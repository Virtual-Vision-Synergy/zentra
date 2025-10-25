# 🚀 Guide de Démarrage - Zentra

## ✅ État du Projet

### Backend (Spring Boot)
- ✅ **Configuration** : `application.properties` configuré
- ✅ **CORS** : Configuration CORS activée pour React
- ✅ **Base de données** : PostgreSQL configurée (port 5432)
- ✅ **API** : Port 8080, context-path `/api`
- ✅ **Compilation** : Projet compile sans erreurs

### Frontend (React + Vite)
- ✅ **Configuration** : Vite configuré
- ✅ **API Client** : axios configuré pour `http://localhost:8080/api`
- ✅ **Port** : 5173 (Vite dev server)

---

## 🔧 Problème Résolu

**Problème initial** : Conflit de bean `CorsConfig` dupliqué
- Il y avait deux fichiers `CorsConfig.java` :
  - ✅ `common/config/CorsConfig.java` (conservé)
  - ❌ `common/middleware/CorsConfig.java` (supprimé)

**Solution** : Fichier dupliqué supprimé avec succès.

---

## 📋 Prérequis

Avant de démarrer, assurez-vous que :

1. **PostgreSQL** est installé et en cours d'exécution
   - Port par défaut : `5432`
   - Base de données : `zentra`
   - Utilisateur : `postgres`
   - Mot de passe : `alan` (configuré dans `application.properties`)

2. **Java 17** est installé
   ```powershell
   java -version
   ```

3. **Node.js** est installé (version 16 ou supérieure)
   ```powershell
   node -version
   npm -version
   ```

---

## 🎯 Méthode 1 : Démarrage avec le Script PowerShell (RECOMMANDÉ)

Le projet contient un script de démarrage automatique :

```powershell
.\start-zentra.ps1
```

### Options disponibles :
1. **Backend uniquement** - Démarre Spring Boot sur le port 8080
2. **Frontend uniquement** - Démarre React + Vite sur le port 5173
3. **Les deux** - Démarre backend ET frontend automatiquement
4. **Installer dépendances** - Exécute `npm install` pour le frontend
5. **Charger données de test** - Charge les données dans PostgreSQL
6. **Quitter**

---

## 🎯 Méthode 2 : Démarrage Manuel

### 1️⃣ Démarrer le Backend

```powershell
# Aller dans le dossier backend
cd "d:\S5\Mr tovo\zentra\zentra-core"

# Démarrer Spring Boot
.\mvnw.cmd spring-boot:run
```

Le backend sera accessible sur : **http://localhost:8080/api**

### 2️⃣ Démarrer le Frontend

Ouvrir un **nouveau terminal** :

```powershell
# Aller dans le dossier frontend
cd "d:\S5\Mr tovo\zentra\zentra-ui"

# Installer les dépendances (première fois uniquement)
npm install

# Démarrer le serveur de développement
npm run dev
```

Le frontend sera accessible sur : **http://localhost:5173**

---

## 🗄️ Initialiser la Base de Données

Si la base de données est vide, chargez les données de test :

```powershell
cd "d:\S5\Mr tovo\zentra\zentra-core"
psql -U postgres -d zentra -f src\main\resources\sql\test_data_staffing_needs.sql
```

---

## 🔍 Vérification

### ✅ Backend fonctionne ?
Ouvrez : http://localhost:8080/api

Vous devriez voir une page (peut-être une erreur 404, c'est normal si vous n'avez pas de route `/`)

### ✅ Frontend fonctionne ?
Ouvrez : http://localhost:5173

Vous devriez voir l'interface React

### ✅ API accessible depuis le frontend ?
Les requêtes du frontend vers `http://localhost:8080/api/*` devraient fonctionner grâce à la configuration CORS.

---

## 📚 Endpoints API Disponibles

### QCM (Questionnaires)
- `GET /api/qcm` - Liste des QCM
- `GET /api/qcm/{id}` - Détails d'un QCM
- `POST /api/qcm` - Créer un QCM
- `PUT /api/qcm/{id}` - Modifier un QCM
- `DELETE /api/qcm/{id}` - Supprimer un QCM

### Staffing Needs (Besoins en Personnel)
- `GET /api/staffing-needs` - Liste des besoins
- `GET /api/staffing-needs/{id}` - Détails d'un besoin
- `POST /api/staffing-needs` - Créer un besoin
- `PUT /api/staffing-needs/{id}` - Modifier un besoin
- `DELETE /api/staffing-needs/{id}` - Supprimer un besoin
- `GET /api/staffing-needs/status/{status}` - Filtrer par statut
- `GET /api/staffing-needs/priority/{priority}` - Filtrer par priorité

---

## 🐛 Résolution des Problèmes

### ❌ "Port 8080 already in use"
Un autre processus utilise le port 8080. Tuez le processus :
```powershell
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force
```

### ❌ "Cannot connect to database"
Vérifiez que PostgreSQL est démarré :
```powershell
Get-Service -Name postgresql*
```

Démarrez PostgreSQL si nécessaire.

### ❌ "npm: command not found"
Installez Node.js depuis https://nodejs.org/

### ❌ Erreur CORS dans le navigateur
La configuration CORS est en place. Assurez-vous que :
- Le backend est bien sur `http://localhost:8080`
- Le frontend est bien sur `http://localhost:5173`

---

## 📁 Structure du Projet

```
zentra/
├── zentra-core/          # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/pentagone/business/zentracore/
│   │   │   │   ├── ZentraCoreApplication.java
│   │   │   │   ├── common/
│   │   │   │   │   └── config/
│   │   │   │   │       └── CorsConfig.java  ✅
│   │   │   │   └── hr/
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── sql/
│   └── pom.xml
│
└── zentra-ui/            # Frontend React + Vite
    ├── src/
    │   ├── App.tsx
    │   ├── services/
    │   │   └── api.ts
    │   ├── hr/
    │   │   └── services/
    │   │       └── staffingNeedService.ts
    │   └── pages/
    └── package.json
```

---

## 🎉 C'est Parti !

Utilisez le script de démarrage pour lancer l'application :

```powershell
.\start-zentra.ps1
```

Choisissez l'option **3** pour démarrer backend ET frontend automatiquement ! 🚀

---

**Dernière mise à jour** : 25 octobre 2025
**Status** : ✅ Backend compilé avec succès | ⚠️ Nécessite PostgreSQL démarré
