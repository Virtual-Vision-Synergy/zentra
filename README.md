# 🏢 Zentra - Application de Gestion d'Entreprise

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![React](https://img.shields.io/badge/React-19.1.1-61dafb)
![TypeScript](https://img.shields.io/badge/TypeScript-5.9.3-blue)

**Zentra** est une application complète de gestion d'entreprise développée dans le cadre d'un projet S5. Elle couvre plusieurs modules métier incluant la gestion des ressources humaines, avec une architecture moderne basée sur Spring Boot et React.

---

## 📋 Table des Matières

- [Modules Disponibles](#-modules-disponibles)
- [Technologies](#-technologies)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Démarrage Rapide](#-démarrage-rapide)
- [Structure du Projet](#-structure-du-projet)
- [API Documentation](#-api-documentation)
- [Fonctionnalités](#-fonctionnalités)
- [Tests](#-tests)

---

## 🎯 Modules Disponibles

### ✅ Module RH - Gestion des Besoins en Personnel

Le module de **Gestion des Besoins en Personnel** permet de :
- Créer et suivre les demandes de recrutement
- Gérer les priorités et statuts des besoins
- Allouer et suivre les budgets de recrutement
- Lier les besoins aux départements et postes
- Suivre la progression des recrutements

**Status** : ✅ Complet (Backend + Frontend)

**Documentation** :
- [Guide Frontend](zentra-ui/FRONTEND_GUIDE.md)
- [API Endpoints](zentra-core/BESOIN_API.md)

### 🔄 Autres Modules RH

Le projet inclut également d'autres modules RH :
- Gestion des QCM (Questionnaires)
- Gestion des candidatures
- Gestion des contrats
- Gestion des entretiens

---

## 🛠 Technologies

### Backend (zentra-core)

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java | 21 | Langage principal |
| Spring Boot | 3.x | Framework backend |
| Spring Data JPA | 3.x | Accès aux données |
| Hibernate | 6.x | ORM |
| MapStruct | 1.5+ | Mapping DTO/Entity |
| Lombok | 1.18+ | Réduction boilerplate |
| PostgreSQL | 15+ | Base de données |
| JUnit 5 | 5.x | Tests unitaires |
| Mockito | 5.x | Mocks pour tests |
| Maven | 3.9+ | Build & dépendances |

### Frontend (zentra-ui)

| Technologie | Version | Usage |
|-------------|---------|-------|
| React | 19.1.1 | UI Framework |
| TypeScript | 5.9.3 | Langage typé |
| Vite | 7.1.7 | Build tool & dev server |
| CSS3 | - | Styling |
| Fetch API | - | Requêtes HTTP |

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    ZENTRA APPLICATION                    │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  ┌─────────────────┐         ┌─────────────────┐       │
│  │  Frontend (UI)  │         │  Backend (API)  │       │
│  │                 │         │                 │       │
│  │  React 19       │◄───────►│  Spring Boot 3  │       │
│  │  TypeScript     │  REST   │  Java 21        │       │
│  │  Vite           │  JSON   │  JPA/Hibernate  │       │
│  │                 │         │                 │       │
│  │  Port: 5173     │         │  Port: 8080     │       │
│  └─────────────────┘         └────────┬────────┘       │
│                                        │                 │
│                                        ▼                 │
│                              ┌─────────────────┐        │
│                              │   PostgreSQL    │        │
│                              │   Database      │        │
│                              │   Port: 5432    │        │
│                              └─────────────────┘        │
└─────────────────────────────────────────────────────────┘
```

### Architecture Backend (Couches)

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database (PostgreSQL)
```

### Architecture Frontend (Composants)

```
App.tsx
    ↓
StaffingNeedApp.tsx (Module Principal)
    ↓
├── StaffingNeedList.tsx (Vue Liste)
├── StaffingNeedForm.tsx (Formulaire)
└── StaffingNeedDetail.tsx (Vue Détail)
```

---

## 📦 Installation

### Prérequis

- ☕ **Java JDK 21+** ([Télécharger](https://adoptium.net/))
- 🐘 **PostgreSQL 15+** ([Télécharger](https://www.postgresql.org/download/))
- 📦 **Node.js 20+** et npm ([Télécharger](https://nodejs.org/))
- 🔧 **Git** ([Télécharger](https://git-scm.com/))
- 💻 **Maven 3.9+** (inclus via wrapper)

### 1. Cloner le projet

```bash
git clone <repository-url>
cd zentra
```

### 2. Configurer la base de données

```sql
-- Créer la base de données
CREATE DATABASE zentra;

-- Se connecter à la base
\c zentra

-- Exécuter les scripts SQL
\i zentra-core/src/main/resources/sql/table_rh.sql
\i zentra-core/src/main/resources/sql/test_data_staffing_needs.sql
```

### 3. Configurer le backend

Éditer `zentra-core/src/main/resources/application.properties` :

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/zentra
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Installer les dépendances frontend

```powershell
cd zentra-ui
npm install
```

---

## 🚀 Démarrage Rapide

### Option 1 : Script PowerShell (Recommandé)

```powershell
# À la racine du projet
.\start-zentra.ps1
```

Le script vous propose :
1. Démarrer le backend seul
2. Démarrer le frontend seul
3. **Démarrer les deux** (recommandé)
4. Installer les dépendances npm
5. Charger les données de test
6. Quitter

### Option 2 : Démarrage manuel

#### Terminal 1 - Backend

```powershell
cd zentra-core
.\mvnw spring-boot:run
```

#### Terminal 2 - Frontend

```powershell
cd zentra-ui
npm run dev
```

### 🌐 Accéder à l'application

- **Frontend** : [http://localhost:5173](http://localhost:5173)
- **Backend API** : [http://localhost:8080/api](http://localhost:8080/api)
- **API Staffing Needs** : [http://localhost:8080/api/staffing-needs](http://localhost:8080/api/staffing-needs)

---

## 📁 Structure du Projet

```
zentra/
├── zentra-core/                      # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/.../hr/
│   │   │   │   ├── controller/      # REST Controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # JPA Entities
│   │   │   │   ├── mapper/          # MapStruct Mappers
│   │   │   │   ├── repository/      # Spring Data Repositories
│   │   │   │   └── service/         # Business Logic
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── sql/             # Scripts SQL
│   │   └── test/                    # Tests unitaires
│   ├── pom.xml                      # Dépendances Maven
│   └── mvnw                         # Maven Wrapper
│
├── zentra-ui/                        # Frontend React
│   ├── src/
│   │   ├── hr/
│   │   │   ├── types/               # TypeScript Types
│   │   │   ├── services/            # API Services
│   │   │   ├── components/          # React Components
│   │   │   └── StaffingNeedApp.tsx  # Main Module
│   │   ├── App.tsx                  # Root Component
│   │   └── main.tsx                 # Entry Point
│   ├── package.json                 # Dépendances npm
│   └── vite.config.ts              # Configuration Vite
│
├── start-zentra.ps1                 # Script de démarrage
├── README.md                        # Ce fichier
└── IMPLEMENTATION_SUMMARY.md        # Résumé de l'implémentation
```

---

## 📡 API Documentation

### Endpoints - Gestion des Besoins en Personnel

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/staffing-needs` | Liste tous les besoins |
| `GET` | `/api/staffing-needs/{id}` | Récupère un besoin par ID |
| `POST` | `/api/staffing-needs` | Crée un nouveau besoin |
| `PUT` | `/api/staffing-needs/{id}` | Met à jour un besoin |
| `DELETE` | `/api/staffing-needs/{id}` | Supprime un besoin |
| `GET` | `/api/staffing-needs/status/{status}` | Filtre par statut |
| `GET` | `/api/staffing-needs/priority/{priority}` | Filtre par priorité |
| `GET` | `/api/staffing-needs/department/{id}` | Filtre par département |
| `GET` | `/api/staffing-needs/stats` | Statistiques globales |

### Exemple de requête - Créer un besoin

```json
POST /api/staffing-needs
Content-Type: application/json

{
  "title": "Développeur Backend Senior",
  "description": "Expertise Spring Boot et microservices requise",
  "numberOfPositions": 2,
  "priority": "High",
  "status": "Open",
  "requiredStartDate": "2026-01-15",
  "budgetAllocated": 120000.00,
  "justification": "Expansion de l'équipe technique",
  "departmentId": 1,
  "jobId": 5
}
```

### Exemple de réponse

```json
{
  "id": 10,
  "title": "Développeur Backend Senior",
  "description": "Expertise Spring Boot et microservices requise",
  "numberOfPositions": 2,
  "priority": "High",
  "status": "Open",
  "requiredStartDate": "2026-01-15",
  "budgetAllocated": 120000.00,
  "justification": "Expansion de l'équipe technique",
  "departmentId": 1,
  "departmentName": "Département IT",
  "jobId": 5,
  "jobTitle": "Développeur Backend",
  "createdAt": "2025-11-15T10:30:00",
  "updatedAt": "2025-11-15T10:30:00"
}
```

**Documentation complète** : [BESOIN_API.md](zentra-core/BESOIN_API.md)

---

## ✨ Fonctionnalités

### Module Gestion des Besoins en Personnel

#### 📋 Liste des Besoins
- Affichage en grille de cartes
- Filtrage par statut (Open, In Progress, Fulfilled, Cancelled)
- Filtrage par priorité (High, Medium, Low)
- Badges colorés pour statut et priorité
- Boutons d'action (Voir, Modifier, Supprimer)

#### ➕ Création de Besoin
- Formulaire avec validation
- Champs obligatoires marqués
- Sélection de département et poste
- Saisie du budget et justification
- Auto-complétion de la date

#### ✏️ Modification de Besoin
- Chargement des données existantes
- Modification de tous les champs
- Validation avant sauvegarde

#### 👁️ Vue Détaillée
- Affichage formaté des informations
- Groupement par sections logiques
- Bouton d'édition rapide

#### 📊 Statistiques
- Nombre total de besoins
- Total des postes demandés
- Budget global alloué
- Répartition par statut et priorité

---

## 🧪 Tests

### Backend - Tests Unitaires

```powershell
cd zentra-core
.\mvnw test
```

**Couverture** : 11 tests pour StaffingNeedService
- Création, lecture, mise à jour, suppression
- Filtrage par statut, priorité, département
- Validation des données
- Gestion des erreurs

### Frontend - Tests Manuels

1. **Créer un besoin**
   - Remplir le formulaire
   - Vérifier la sauvegarde
   - Vérifier l'apparition dans la liste

2. **Filtrer les besoins**
   - Filtrer par statut
   - Filtrer par priorité
   - Vérifier les résultats

3. **Modifier un besoin**
   - Cliquer sur "Modifier"
   - Changer des valeurs
   - Vérifier les modifications

4. **Supprimer un besoin**
   - Cliquer sur "Supprimer"
   - Confirmer la suppression
   - Vérifier la disparition

---

## 🤝 Contribution

Ce projet est un projet scolaire S5. Pour toute question ou suggestion :

1. Créer une issue
2. Proposer une pull request
3. Contacter l'équipe de développement

---

## 📄 Licence

Projet académique - Tous droits réservés

---

## 🙏 Remerciements

- **Équipe pédagogique S5**
- **Spring Framework Community**
- **React Community**
- **Contributors**

---

## 📞 Support

Pour toute assistance :
- 📖 Consulter [FRONTEND_GUIDE.md](zentra-ui/FRONTEND_GUIDE.md)
- 📖 Consulter [BESOIN_API.md](zentra-core/BESOIN_API.md)
- 🐛 Créer une issue sur GitHub

---

**Made with ❤️ by the Zentra Team**
