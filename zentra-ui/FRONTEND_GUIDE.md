# 🚀 Guide de Démarrage - Interface Utilisateur Zentra

## 📋 Ce qui a été créé

### Frontend React + TypeScript
- ✅ Application complète de gestion des besoins en personnel
- ✅ 3 composants React principaux
- ✅ Service API pour communiquer avec le backend
- ✅ Types TypeScript
- ✅ Styles CSS modernes

### Structure créée

```
zentra-ui/src/hr/
├── types/
│   └── StaffingNeed.ts              # Types TypeScript
├── services/
│   └── staffingNeedService.ts       # Service API
├── components/
│   ├── StaffingNeedList.tsx         # Liste des besoins
│   ├── StaffingNeedList.css
│   ├── StaffingNeedForm.tsx         # Formulaire de création/édition
│   ├── StaffingNeedForm.css
│   ├── StaffingNeedDetail.tsx       # Vue détaillée
│   └── StaffingNeedDetail.css
├── StaffingNeedApp.tsx              # Application principale
└── StaffingNeedApp.css
```

---

## 🔧 Étape 1 : Démarrer le Backend (zentra-core)

### Terminal 1 - Backend

```powershell
# Se placer dans zentra-core
cd "d:\S5\Mr tovo\zentra\zentra-core"

# Démarrer l'application Spring Boot
./mvnw spring-boot:run
```

**Vérification** : L'API doit être accessible sur `http://localhost:8080/api`

---

## 🎨 Étape 2 : Démarrer le Frontend (zentra-ui)

### Terminal 2 - Frontend

```powershell
# Se placer dans zentra-ui
cd "d:\S5\Mr tovo\zentra\zentra-ui"

# Installer les dépendances (première fois seulement)
npm install

# Démarrer le serveur de développement
npm run dev
```

**Le frontend sera accessible sur** : `http://localhost:5173`

---

## ⚙️ Étape 3 : Configuration CORS (Backend)

Pour permettre au frontend de communiquer avec le backend, ajoutez cette configuration :

### Créer : `zentra-core/src/main/java/org/pentagone/business/zentracore/common/config/CorsConfig.java`

```java
package org.pentagone.business.zentracore.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // Frontend URL
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

**Puis redémarrer le backend !**

---

## 🎯 Étape 4 : Tester l'application

### 1. Ouvrir le navigateur
```
http://localhost:5173
```

### 2. Fonctionnalités disponibles

#### 📋 Vue Liste
- Voir tous les besoins en personnel
- Filtrer par statut (Open, In Progress, Fulfilled, Cancelled)
- Filtrer par priorité (High, Medium, Low)
- Actions : Voir, Modifier, Supprimer

#### ➕ Créer un besoin
- Cliquer sur "Nouveau Besoin"
- Remplir le formulaire
- Sauvegarder

#### ✏️ Modifier un besoin
- Cliquer sur "Modifier" sur une carte
- Modifier les informations
- Sauvegarder

#### 👁️ Vue détaillée
- Cliquer sur "Voir" sur une carte
- Consulter toutes les informations
- Possibilité de modifier depuis cette vue

---

## 📊 Prérequis pour tester

### Données de test dans la base de données

```sql
-- Se connecter à PostgreSQL
psql -U postgres -d zentra

-- Insérer un département
INSERT INTO department (id, name, description, annual_budget, created_at, updated_at)
VALUES (1, 'IT Department', 'Technologies de l''information', 500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insérer un poste
INSERT INTO job (id, title, description, required_degree, required_skills, department_id, created_date, updated_at)
VALUES (1, 'Développeur Backend', 'Développeur Spring Boot Senior', 'Master', 'Java, Spring, PostgreSQL', 1, CURRENT_DATE, CURRENT_DATE);

-- Créer un besoin de test
INSERT INTO staffing_need (title, description, number_of_positions, priority, status, 
                           required_start_date, budget_allocated, justification,
                           department_id, job_id, created_at, updated_at)
VALUES ('Développeur Backend Senior', 
        'Besoin urgent de développeur avec expertise Spring Boot et microservices',
        2, 'High', 'Open',
        '2025-11-15', 120000.00,
        'Extension de l''équipe technique pour nouveau projet',
        1, 1,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

## 🔍 Résolution de problèmes

### Problème : Erreur CORS
**Solution** : Vérifier que le fichier `CorsConfig.java` est créé et le backend redémarré

### Problème : Cannot connect to backend
**Solution** :
```powershell
# Vérifier que le backend est démarré
# Dans zentra-core
./mvnw spring-boot:run
```

### Problème : Port 5173 déjà utilisé
**Solution** :
Vite utilisera automatiquement le prochain port disponible (5174, 5175, etc.)

### Problème : Département/Poste non trouvé
**Solution** :
Vérifier que les départements et postes existent dans la base de données
```sql
SELECT * FROM department;
SELECT * FROM job;
```

---

## 📱 Captures d'écran des fonctionnalités

### 1. Vue Liste
- Grille de cartes avec les besoins
- Filtres en haut
- Badges de priorité et statut colorés

### 2. Formulaire
- Champs organisés en grilles
- Validation en temps réel
- Champs obligatoires marqués

### 3. Vue Détaillée
- Header coloré avec badges
- Informations groupées par sections
- Actions : Modifier, Fermer

---

## 🎨 Personnalisation

### Modifier les couleurs
Éditer les fichiers CSS dans `src/hr/components/`

### Modifier l'URL de l'API
Éditer : `src/hr/services/staffingNeedService.ts`
```typescript
const API_BASE_URL = 'http://localhost:8080/api';
```

---

## 📚 Structure de l'interface

```
┌─────────────────────────────────────────┐
│  Header (Titre + Bouton Nouveau)       │
├─────────────────────────────────────────┤
│  Filtres (Statut, Priorité, Refresh)  │
├─────────────────────────────────────────┤
│  ┌──────┐  ┌──────┐  ┌──────┐         │
│  │Card 1│  │Card 2│  │Card 3│         │
│  │      │  │      │  │      │         │
│  │Title │  │Title │  │Title │         │
│  │Info  │  │Info  │  │Info  │         │
│  │Badges│  │Badges│  │Badges│         │
│  │Btn...│  │Btn...│  │Btn...│         │
│  └──────┘  └──────┘  └──────┘         │
└─────────────────────────────────────────┘
```

---

## ✅ Checklist de démarrage

- [ ] Backend Spring Boot démarré (port 8080)
- [ ] Base de données PostgreSQL active
- [ ] Tables créées (script SQL exécuté)
- [ ] Configuration CORS ajoutée
- [ ] Données de test insérées
- [ ] npm install exécuté dans zentra-ui
- [ ] Frontend démarré (npm run dev)
- [ ] Browser ouvert sur http://localhost:5173
- [ ] Vérifier que les besoins s'affichent

---

## 🚀 Commandes rapides

### Terminal 1 (Backend)
```powershell
cd "d:\S5\Mr tovo\zentra\zentra-core"
./mvnw spring-boot:run
```

### Terminal 2 (Frontend)
```powershell
cd "d:\S5\Mr tovo\zentra\zentra-ui"
npm run dev
```

---

## 🎉 Prêt à utiliser !

Une fois les deux serveurs démarrés :
1. Ouvrez http://localhost:5173
2. L'interface de gestion des besoins en personnel s'affiche
3. Créez, modifiez, consultez et supprimez des besoins
4. Les données sont sauvegardées dans PostgreSQL

**Bon développement ! 🚀**
