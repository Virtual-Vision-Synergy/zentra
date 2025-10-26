# ============================================
# SCRIPT DE DÉMARRAGE ZENTRA
# ============================================

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "   DÉMARRAGE DE L'APPLICATION ZENTRA" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Chemins
$backendPath = "d:\S5\Mr tovo\zentra\zentra-core"
$frontendPath = "d:\S5\Mr tovo\zentra\zentra-ui"

# Fonction pour vérifier si un port est utilisé
function Test-Port {
    param([int]$Port)
    $connection = New-Object System.Net.Sockets.TcpClient
    try {
        $connection.Connect("localhost", $Port)
        $connection.Close()
        return $true
    }
    catch {
        return $false
    }
}

# Menu principal
Write-Host "Que voulez-vous faire ?" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Démarrer le BACKEND (Spring Boot - Port 8080)" -ForegroundColor Green
Write-Host "2. Démarrer le FRONTEND (React + Vite - Port 5173)" -ForegroundColor Green
Write-Host "3. Démarrer les DEUX (Backend + Frontend)" -ForegroundColor Green
Write-Host "4. Installer les dépendances frontend (npm install)" -ForegroundColor Magenta
Write-Host "5. Charger les données de test dans la base" -ForegroundColor Cyan
Write-Host "6. Quitter" -ForegroundColor Red
Write-Host ""

$choice = Read-Host "Votre choix (1-6)"

switch ($choice) {
    "1" {
        Write-Host "`n[BACKEND] Démarrage du backend Spring Boot..." -ForegroundColor Green
        
        if (Test-Port 8080) {
            Write-Host "[ERREUR] Le port 8080 est déjà utilisé !" -ForegroundColor Red
            Write-Host "Un serveur est peut-être déjà en cours d'exécution." -ForegroundColor Yellow
            exit
        }
        
        Set-Location $backendPath
        Write-Host "[INFO] Répertoire: $backendPath" -ForegroundColor Cyan
        Write-Host "[INFO] Compilation et démarrage en cours..." -ForegroundColor Cyan
        Write-Host ""
        
        & .\mvnw.cmd spring-boot:run
    }
    
    "2" {
        Write-Host "`n[FRONTEND] Démarrage du frontend React..." -ForegroundColor Green
        
        Set-Location $frontendPath
        Write-Host "[INFO] Répertoire: $frontendPath" -ForegroundColor Cyan
        
        # Vérifier si node_modules existe
        if (-not (Test-Path "node_modules")) {
            Write-Host "[ATTENTION] node_modules non trouvé !" -ForegroundColor Yellow
            Write-Host "[INFO] Installation des dépendances..." -ForegroundColor Cyan
            npm install
            Write-Host ""
        }
        
        Write-Host "[INFO] Démarrage du serveur de développement..." -ForegroundColor Cyan
        Write-Host ""
        
        npm run dev
    }
    
    "3" {
        Write-Host "`n[FULL STACK] Démarrage Backend + Frontend..." -ForegroundColor Green
        Write-Host ""
        Write-Host "[INFO] Deux fenêtres de terminal vont s'ouvrir:" -ForegroundColor Cyan
        Write-Host "  - Terminal 1: Backend (Spring Boot)" -ForegroundColor White
        Write-Host "  - Terminal 2: Frontend (React + Vite)" -ForegroundColor White
        Write-Host ""
        
        # Vérifier les ports
        if (Test-Port 8080) {
            Write-Host "[ERREUR] Le port 8080 (backend) est déjà utilisé !" -ForegroundColor Red
            exit
        }
        
        # Démarrer le backend dans un nouveau terminal
        Write-Host "[1/2] Ouverture du terminal Backend..." -ForegroundColor Cyan
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$backendPath'; Write-Host '[BACKEND] Démarrage...' -ForegroundColor Green; .\mvnw.cmd spring-boot:run"
        
        # Attendre 5 secondes
        Write-Host "[INFO] Attente de 5 secondes avant de démarrer le frontend..." -ForegroundColor Yellow
        Start-Sleep -Seconds 5
        
        # Vérifier si node_modules existe
        Set-Location $frontendPath
        if (-not (Test-Path "node_modules")) {
            Write-Host "[INFO] Installation des dépendances frontend..." -ForegroundColor Cyan
            npm install
        }
        
        # Démarrer le frontend dans un nouveau terminal
        Write-Host "[2/2] Ouverture du terminal Frontend..." -ForegroundColor Cyan
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$frontendPath'; Write-Host '[FRONTEND] Démarrage...' -ForegroundColor Green; npm run dev"
        
        Write-Host ""
        Write-Host "================================================" -ForegroundColor Cyan
        Write-Host "   APPLICATION DÉMARRÉE !" -ForegroundColor Green
        Write-Host "================================================" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Backend API : http://localhost:8080/api" -ForegroundColor White
        Write-Host "Frontend UI : http://localhost:5173" -ForegroundColor White
        Write-Host ""
        Write-Host "Attendez quelques secondes que les serveurs démarrent..." -ForegroundColor Yellow
        Write-Host ""
    }
    
    "4" {
        Write-Host "`n[NPM] Installation des dépendances frontend..." -ForegroundColor Magenta
        
        Set-Location $frontendPath
        Write-Host "[INFO] Répertoire: $frontendPath" -ForegroundColor Cyan
        Write-Host ""
        
        npm install
        
        Write-Host ""
        Write-Host "[SUCCESS] Dépendances installées avec succès !" -ForegroundColor Green
        Write-Host "Vous pouvez maintenant démarrer le frontend." -ForegroundColor White
    }
    
    "5" {
        Write-Host "`n[BASE DE DONNÉES] Chargement des données de test..." -ForegroundColor Cyan
        Write-Host ""
        
        $sqlFile = "$backendPath\src\main\resources\sql\test_data_staffing_needs.sql"
        
        if (-not (Test-Path $sqlFile)) {
            Write-Host "[ERREUR] Fichier SQL non trouvé: $sqlFile" -ForegroundColor Red
            exit
        }
        
        Write-Host "[INFO] Fichier SQL: $sqlFile" -ForegroundColor White
        Write-Host ""
        
        # Demander les informations de connexion
        $dbName = Read-Host "Nom de la base de données [zentra]"
        if ([string]::IsNullOrWhiteSpace($dbName)) { $dbName = "zentra" }
        
        $dbUser = Read-Host "Utilisateur PostgreSQL [postgres]"
        if ([string]::IsNullOrWhiteSpace($dbUser)) { $dbUser = "postgres" }
        
        Write-Host ""
        Write-Host "[INFO] Connexion à PostgreSQL..." -ForegroundColor Cyan
        Write-Host "[INFO] Exécution du script SQL..." -ForegroundColor Cyan
        Write-Host ""
        
        # Exécuter le script SQL
        & psql -U $dbUser -d $dbName -f $sqlFile
        
        Write-Host ""
        Write-Host "[SUCCESS] Données de test chargées !" -ForegroundColor Green
    }
    
    "6" {
        Write-Host "`nAu revoir ! 👋" -ForegroundColor Cyan
        exit
    }
    
    default {
        Write-Host "`n[ERREUR] Choix invalide. Veuillez choisir entre 1 et 6." -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Appuyez sur une touche pour fermer..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
