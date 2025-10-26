# API - Gestion des Besoins en Personnel

## Description
Cette API permet de gérer les besoins en personnel (staffing needs) de l'entreprise. Elle offre des fonctionnalités complètes de CRUD (Create, Read, Update, Delete) pour la gestion des demandes de recrutement.

## Base URL
```
/staffing-needs
```

## Endpoints

### 1. Créer un besoin en personnel
**POST** `/staffing-needs`

**Request Body:**
```json
{
  "title": "Développeur Backend Senior",
  "description": "Besoin urgent de développeur avec expertise Spring Boot",
  "numberOfPositions": 2,
  "priority": "High",
  "status": "Open",
  "requiredStartDate": "2025-11-01",
  "budgetAllocated": 120000.00,
  "justification": "Extension de l'équipe technique pour nouveau projet",
  "departmentId": 1,
  "jobId": 5,
  "requestedById": 10
}
```

**Response:** `201 CREATED`
```json
{
  "id": 1,
  "title": "Développeur Backend Senior",
  "description": "Besoin urgent de développeur avec expertise Spring Boot",
  "numberOfPositions": 2,
  "priority": "High",
  "status": "Open",
  "requiredStartDate": "2025-11-01",
  "budgetAllocated": 120000.00,
  "justification": "Extension de l'équipe technique pour nouveau projet",
  "departmentId": 1,
  "departmentName": "IT Department",
  "jobId": 5,
  "jobTitle": "Senior Backend Developer",
  "requestedById": 10,
  "requestedByName": "Jean Dupont",
  "createdAt": "2025-10-25T10:30:00",
  "updatedAt": "2025-10-25T10:30:00"
}
```

---

### 2. Mettre à jour un besoin en personnel
**PUT** `/staffing-needs/{id}`

**Path Parameters:**
- `id` (Long): ID du besoin en personnel

**Request Body:**
```json
{
  "title": "Développeur Backend Senior",
  "description": "Besoin urgent de développeur avec expertise Spring Boot et microservices",
  "numberOfPositions": 3,
  "priority": "High",
  "status": "In Progress",
  "requiredStartDate": "2025-11-01",
  "budgetAllocated": 150000.00,
  "justification": "Extension de l'équipe technique pour nouveau projet",
  "departmentId": 1,
  "jobId": 5,
  "requestedById": 10
}
```

**Response:** `200 OK`

---

### 3. Obtenir un besoin par ID
**GET** `/staffing-needs/{id}`

**Path Parameters:**
- `id` (Long): ID du besoin en personnel

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Développeur Backend Senior",
  "description": "Besoin urgent de développeur avec expertise Spring Boot",
  "numberOfPositions": 2,
  "priority": "High",
  "status": "Open",
  "requiredStartDate": "2025-11-01",
  "budgetAllocated": 120000.00,
  "justification": "Extension de l'équipe technique pour nouveau projet",
  "departmentId": 1,
  "departmentName": "IT Department",
  "jobId": 5,
  "jobTitle": "Senior Backend Developer",
  "requestedById": 10,
  "requestedByName": "Jean Dupont",
  "createdAt": "2025-10-25T10:30:00",
  "updatedAt": "2025-10-25T10:30:00"
}
```

---

### 4. Obtenir tous les besoins en personnel
**GET** `/staffing-needs`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Développeur Backend Senior",
    "numberOfPositions": 2,
    "priority": "High",
    "status": "Open",
    "departmentName": "IT Department",
    "jobTitle": "Senior Backend Developer",
    ...
  },
  {
    "id": 2,
    "title": "Chef de projet",
    "numberOfPositions": 1,
    "priority": "Medium",
    "status": "In Progress",
    "departmentName": "Management",
    "jobTitle": "Project Manager",
    ...
  }
]
```

---

### 5. Obtenir les besoins par département
**GET** `/staffing-needs/department/{departmentId}`

**Path Parameters:**
- `departmentId` (Long): ID du département

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Développeur Backend Senior",
    "numberOfPositions": 2,
    "departmentId": 1,
    "departmentName": "IT Department",
    ...
  }
]
```

---

### 6. Obtenir les besoins par poste
**GET** `/staffing-needs/job/{jobId}`

**Path Parameters:**
- `jobId` (Long): ID du poste

**Response:** `200 OK`

---

### 7. Obtenir les besoins par statut
**GET** `/staffing-needs/status/{status}`

**Path Parameters:**
- `status` (String): Statut du besoin
  - Valeurs possibles: `Open`, `In Progress`, `Fulfilled`, `Cancelled`

**Response:** `200 OK`

---

### 8. Obtenir les besoins par priorité
**GET** `/staffing-needs/priority/{priority}`

**Path Parameters:**
- `priority` (String): Priorité du besoin
  - Valeurs possibles: `High`, `Medium`, `Low`

**Response:** `200 OK`

---

### 9. Supprimer un besoin en personnel
**DELETE** `/staffing-needs/{id}`

**Path Parameters:**
- `id` (Long): ID du besoin en personnel

**Response:** `204 NO CONTENT`

---

## Modèle de données

### StaffingNeedDto

| Champ | Type | Description | Obligatoire |
|-------|------|-------------|-------------|
| id | Long | Identifiant unique | Auto-généré |
| title | String | Titre du besoin | Oui |
| description | String | Description détaillée | Non |
| numberOfPositions | Integer | Nombre de postes requis | Oui (> 0) |
| priority | String | Priorité (High/Medium/Low) | Non |
| status | String | Statut (Open/In Progress/Fulfilled/Cancelled) | Oui |
| requiredStartDate | LocalDate | Date de début souhaitée | Non |
| budgetAllocated | Double | Budget alloué | Non |
| justification | String | Justification du besoin | Non |
| departmentId | Long | ID du département | Oui |
| departmentName | String | Nom du département | Lecture seule |
| jobId | Long | ID du poste | Oui |
| jobTitle | String | Titre du poste | Lecture seule |
| requestedById | Long | ID de l'employé demandeur | Non |
| requestedByName | String | Nom de l'employé demandeur | Lecture seule |
| createdAt | LocalDateTime | Date de création | Auto-généré |
| updatedAt | LocalDateTime | Date de modification | Auto-généré |

---

## Codes de réponse HTTP

| Code | Description |
|------|-------------|
| 200 | Succès - Ressource récupérée ou mise à jour |
| 201 | Créé - Nouvelle ressource créée avec succès |
| 204 | Aucun contenu - Suppression réussie |
| 400 | Mauvaise requête - Données invalides |
| 404 | Non trouvé - Ressource inexistante |
| 500 | Erreur serveur interne |

---

## Règles de validation

1. **title** : Ne peut pas être vide
2. **numberOfPositions** : Doit être supérieur à 0
3. **status** : Ne peut pas être vide (défaut: "Open")
4. **departmentId** : Doit référencer un département existant
5. **jobId** : Doit référencer un poste existant
6. **requestedById** : Si fourni, doit référencer un employé existant

---

## Exemples de cas d'usage

### Cas 1 : Créer un besoin urgent
```bash
curl -X POST http://localhost:8080/staffing-needs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Ingénieur DevOps",
    "numberOfPositions": 1,
    "priority": "High",
    "status": "Open",
    "departmentId": 2,
    "jobId": 8
  }'
```

### Cas 2 : Filtrer les besoins ouverts
```bash
curl http://localhost:8080/staffing-needs/status/Open
```

### Cas 3 : Voir tous les besoins d'un département
```bash
curl http://localhost:8080/staffing-needs/department/1
```

---

## Notes techniques

- L'API utilise Spring Boot avec JPA/Hibernate
- Les transactions sont gérées automatiquement
- MapStruct est utilisé pour la conversion Entity ↔ DTO
- La gestion des erreurs est centralisée via `GlobalExceptionHandler`
- Les timestamps sont gérés automatiquement par JPA (`@PrePersist`, `@PreUpdate`)
