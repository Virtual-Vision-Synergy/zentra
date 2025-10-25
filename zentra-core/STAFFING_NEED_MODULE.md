# Module de Gestion des Besoins en Personnel - Zentra Core

## 📋 Vue d'ensemble

Ce module implémente la gestion complète des **besoins en personnel** (staffing needs) pour le système Zentra. Il permet aux départements de créer, suivre et gérer leurs demandes de recrutement via une API REST complète.

## 🏗️ Architecture

Le module suit l'architecture en couches standard du projet Zentra Core :

```
hr/
├── entity/
│   └── StaffingNeed.java          # Entité JPA
├── dto/
│   └── StaffingNeedDto.java       # Objet de transfert de données
├── repository/
│   └── StaffingNeedRepository.java # Interface JPA Repository
├── mapper/
│   └── StaffingNeedMapper.java    # MapStruct mapper
├── service/
│   ├── StaffingNeedService.java        # Interface service
│   └── impl/
│       └── StaffingNeedServiceImpl.java # Implémentation service
└── controller/
    └── StaffingNeedController.java # Contrôleur REST
```

## 📦 Composants créés

### 1. **Entity - StaffingNeed**
- Entité JPA persistante
- Hérite de `BaseEntity` (id, createdAt, updatedAt)
- Relations avec Department, Job et Employee
- Champs principaux :
  - title, description
  - numberOfPositions
  - priority (High/Medium/Low)
  - status (Open/In Progress/Fulfilled/Cancelled)
  - requiredStartDate
  - budgetAllocated
  - justification

### 2. **DTO - StaffingNeedDto**
- Objet de transfert pour l'API
- Inclut les IDs et noms des entités liées
- Timestamps pour audit

### 3. **Repository - StaffingNeedRepository**
- Extends JpaRepository
- Méthodes de recherche personnalisées :
  - `findByDepartmentId()`
  - `findByJobId()`
  - `findByStatus()`
  - `findByPriority()`

### 4. **Mapper - StaffingNeedMapper**
- Interface MapStruct
- Conversion bidirectionnelle Entity ↔ DTO
- Gestion automatique des relations

### 5. **Service - StaffingNeedService**
- Interface définissant les opérations métier
- 9 méthodes CRUD et de recherche

### 6. **ServiceImpl - StaffingNeedServiceImpl**
- Implémentation du service
- Validation complète des données
- Gestion des transactions
- Vérification de l'existence des entités liées

### 7. **Controller - StaffingNeedController**
- Contrôleur REST
- 9 endpoints exposés
- Codes HTTP appropriés

## 🗄️ Base de données

### Table créée : `staffing_need`

```sql
CREATE TABLE staffing_need (
    id                   SERIAL PRIMARY KEY,
    title                VARCHAR(200) NOT NULL,
    description          TEXT,
    number_of_positions  INTEGER NOT NULL,
    priority             VARCHAR(50),
    status               VARCHAR(50) NOT NULL,
    required_start_date  DATE,
    budget_allocated     DECIMAL(15, 2),
    justification        TEXT,
    department_id        INTEGER NOT NULL REFERENCES department,
    job_id               INTEGER NOT NULL REFERENCES job,
    requested_by         INTEGER REFERENCES employee,
    created_at           TIMESTAMP NOT NULL,
    updated_at           TIMESTAMP NOT NULL
);
```

## 🔌 API Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/staffing-needs` | Créer un besoin |
| PUT | `/staffing-needs/{id}` | Mettre à jour un besoin |
| GET | `/staffing-needs/{id}` | Obtenir un besoin par ID |
| GET | `/staffing-needs` | Obtenir tous les besoins |
| GET | `/staffing-needs/department/{departmentId}` | Besoins par département |
| GET | `/staffing-needs/job/{jobId}` | Besoins par poste |
| GET | `/staffing-needs/status/{status}` | Besoins par statut |
| GET | `/staffing-needs/priority/{priority}` | Besoins par priorité |
| DELETE | `/staffing-needs/{id}` | Supprimer un besoin |

## ✅ Validations implémentées

1. **title** : Obligatoire, non vide
2. **numberOfPositions** : Obligatoire, > 0
3. **status** : Obligatoire (défaut: "Open")
4. **departmentId** : Doit référencer un département existant
5. **jobId** : Doit référencer un poste existant
6. **requestedById** : Si fourni, doit référencer un employé existant

## 🎯 Fonctionnalités

### ✨ CRUD complet
- ✅ Create (POST)
- ✅ Read (GET - par ID, tous, par département, par poste, par statut, par priorité)
- ✅ Update (PUT)
- ✅ Delete (DELETE)

### 🔍 Recherche avancée
- Par département
- Par poste
- Par statut (Open, In Progress, Fulfilled, Cancelled)
- Par priorité (High, Medium, Low)

### 🛡️ Sécurité et validation
- Validation des données entrantes
- Vérification de l'existence des entités liées
- Gestion des erreurs centralisée
- Transactions automatiques

### 📊 Audit
- Timestamps automatiques (createdAt, updatedAt)
- Traçabilité via `requestedBy`

## 🚀 Utilisation

### Exemple de création d'un besoin

```bash
curl -X POST http://localhost:8080/staffing-needs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Développeur Full Stack",
    "description": "Besoin urgent pour projet e-commerce",
    "numberOfPositions": 2,
    "priority": "High",
    "status": "Open",
    "requiredStartDate": "2025-11-15",
    "budgetAllocated": 100000.00,
    "justification": "Nouveau projet stratégique",
    "departmentId": 1,
    "jobId": 3,
    "requestedById": 5
  }'
```

### Exemple de recherche par statut

```bash
curl http://localhost:8080/staffing-needs/status/Open
```

## 📝 Documentation

Voir le fichier [STAFFING_NEED_API.md](./STAFFING_NEED_API.md) pour la documentation complète de l'API.

## 🔧 Technologies utilisées

- **Spring Boot** : Framework principal
- **Spring Data JPA** : Accès aux données
- **Hibernate** : ORM
- **MapStruct** : Mapping Entity/DTO
- **Lombok** : Réduction du boilerplate
- **PostgreSQL** : Base de données

## ✔️ Tests recommandés

1. Créer un besoin avec toutes les données
2. Créer un besoin avec données minimales
3. Mettre à jour un besoin existant
4. Supprimer un besoin
5. Rechercher par département
6. Rechercher par statut
7. Valider les contraintes (nombre de positions > 0)
8. Tester avec des IDs invalides
9. Tester la cascade des relations

## 🎨 Prochaines étapes possibles

- [ ] Ajouter des notifications par email
- [ ] Implémenter un workflow d'approbation
- [ ] Ajouter des statistiques et dashboards
- [ ] Créer des rapports PDF
- [ ] Intégrer avec le module de publication
- [ ] Ajouter la gestion des commentaires
- [ ] Implémenter l'historique des modifications

---

**Date de création** : 25 octobre 2025  
**Version** : 1.0.0  
**Auteur** : Zentra Development Team
