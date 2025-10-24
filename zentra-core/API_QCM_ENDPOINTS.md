# API Documentation - Module QCM (Test Evaluation)

## Base URL
All endpoints are prefixed with: `/api/tests-evaluation`

## Endpoints

### 1. Get All Tests
**GET** `/api/tests-evaluation`

Retrieves all test evaluations in the system.

**Response:** `200 OK`
```json
[
  {
    "idTest": 1,
    "idCandidature": 5,
    "typeTest": "Technique",
    "nomTest": "Test JavaScript",
    "description": "Test des compétences en JavaScript",
    "dateTest": "2025-11-01",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "dureeMinutes": 120,
    "scoreObtenu": 85.50,
    "scoreRequis": 70.00,
    "reussi": true,
    "commentaire": "Très bonnes compétences",
    "evaluateur": "Marie Martin"
  }
]
```

---

### 2. Get Test by ID
**GET** `/api/tests-evaluation/{id}`

Retrieves a specific test evaluation by its ID.

**Path Parameters:**
- `id` (Long) - The test ID

**Response:** `200 OK` or `404 NOT FOUND`
```json
{
  "idTest": 1,
  "idCandidature": 5,
  "typeTest": "Technique",
  "nomTest": "Test JavaScript",
  "description": "Test des compétences en JavaScript",
  "dateTest": "2025-11-01",
  "heureDebut": "14:00:00",
  "heureFin": "16:00:00",
  "dureeMinutes": 120,
  "scoreObtenu": 85.50,
  "scoreRequis": 70.00,
  "reussi": true,
  "commentaire": "Très bonnes compétences",
  "evaluateur": "Marie Martin"
}
```

---

### 3. Get Tests by Candidature
**GET** `/api/tests-evaluation/candidature/{idCandidature}`

Retrieves all tests for a specific candidature.

**Path Parameters:**
- `idCandidature` (Long) - The candidature ID

**Response:** `200 OK`
```json
[
  {
    "idTest": 1,
    "idCandidature": 5,
    "typeTest": "Technique",
    "nomTest": "Test JavaScript",
    "description": "Test des compétences en JavaScript",
    "dateTest": "2025-11-01",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "dureeMinutes": 120,
    "scoreObtenu": 85.50,
    "scoreRequis": 70.00,
    "reussi": true,
    "commentaire": "Très bonnes compétences",
    "evaluateur": "Marie Martin"
  }
]
```

---

### 4. Get Tests by Type
**GET** `/api/tests-evaluation/type/{typeTest}`

Retrieves all tests of a specific type.

**Path Parameters:**
- `typeTest` (String) - The test type (Technique, Psychotechnique, Langue, Personnalité, Cas pratique, Autre)

**Response:** `200 OK`
```json
[
  {
    "idTest": 1,
    "idCandidature": 5,
    "typeTest": "Technique",
    "nomTest": "Test JavaScript",
    "description": "Test des compétences en JavaScript",
    "dateTest": "2025-11-01",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "dureeMinutes": 120,
    "scoreObtenu": 85.50,
    "scoreRequis": 70.00,
    "reussi": true,
    "commentaire": "Très bonnes compétences",
    "evaluateur": "Marie Martin"
  }
]
```

---

### 5. Get Tests by Success Status
**GET** `/api/tests-evaluation/reussi/{reussi}`

Retrieves all tests filtered by success status.

**Path Parameters:**
- `reussi` (Boolean) - true for passed tests, false for failed tests

**Response:** `200 OK`
```json
[
  {
    "idTest": 1,
    "idCandidature": 5,
    "typeTest": "Technique",
    "nomTest": "Test JavaScript",
    "description": "Test des compétences en JavaScript",
    "dateTest": "2025-11-01",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "dureeMinutes": 120,
    "scoreObtenu": 85.50,
    "scoreRequis": 70.00,
    "reussi": true,
    "commentaire": "Très bonnes compétences",
    "evaluateur": "Marie Martin"
  }
]
```

---

### 6. Create Test
**POST** `/api/tests-evaluation`

Creates a new test evaluation.

**Request Body:**
```json
{
  "idCandidature": 5,
  "typeTest": "Technique",
  "nomTest": "Test JavaScript",
  "description": "Test des compétences en JavaScript",
  "dateTest": "2025-11-01",
  "heureDebut": "14:00:00",
  "heureFin": "16:00:00",
  "dureeMinutes": 120,
  "scoreObtenu": 85.50,
  "scoreRequis": 70.00,
  "reussi": true,
  "commentaire": "Très bonnes compétences",
  "evaluateur": "Marie Martin"
}
```

**Response:** `201 CREATED`
```json
{
  "idTest": 1,
  "idCandidature": 5,
  "typeTest": "Technique",
  "nomTest": "Test JavaScript",
  "description": "Test des compétences en JavaScript",
  "dateTest": "2025-11-01",
  "heureDebut": "14:00:00",
  "heureFin": "16:00:00",
  "dureeMinutes": 120,
  "scoreObtenu": 85.50,
  "scoreRequis": 70.00,
  "reussi": true,
  "commentaire": "Très bonnes compétences",
  "evaluateur": "Marie Martin"
}
```

---

### 7. Update Test
**PUT** `/api/tests-evaluation/{id}`

Updates an existing test evaluation.

**Path Parameters:**
- `id` (Long) - The test ID

**Request Body:**
```json
{
  "scoreObtenu": 90.00,
  "reussi": true,
  "commentaire": "Excellentes compétences"
}
```

**Response:** `200 OK` or `404 NOT FOUND`
```json
{
  "idTest": 1,
  "idCandidature": 5,
  "typeTest": "Technique",
  "nomTest": "Test JavaScript",
  "description": "Test des compétences en JavaScript",
  "dateTest": "2025-11-01",
  "heureDebut": "14:00:00",
  "heureFin": "16:00:00",
  "dureeMinutes": 120,
  "scoreObtenu": 90.00,
  "scoreRequis": 70.00,
  "reussi": true,
  "commentaire": "Excellentes compétences",
  "evaluateur": "Marie Martin"
}
```

---

### 8. Delete Test
**DELETE** `/api/tests-evaluation/{id}`

Deletes a test evaluation.

**Path Parameters:**
- `id` (Long) - The test ID

**Response:** `204 NO CONTENT` or `404 NOT FOUND`

---

## Test Types
Valid values for `typeTest`:
- `Technique`
- `Psychotechnique`
- `Langue`
- `Personnalité`
- `Cas pratique`
- `Autre`

## Field Validations
- `scoreObtenu` and `scoreRequis`: Between 0 and 100
- `dureeMinutes`: Between 1 and 480 (max 8 hours)
- `dateTest`: Required, cannot be more than 1 year in the past
- `heureFin` must be after `heureDebut` if both are provided

## Error Responses

### 400 Bad Request
```json
{
  "status": 400,
  "message": "Invalid input parameters"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Test d'évaluation non trouvé avec l'ID: 123"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "Internal server error message"
}
```

## Usage Examples

### cURL Examples

#### Get all tests
```bash
curl -X GET http://localhost:8080/api/tests-evaluation
```

#### Get test by ID
```bash
curl -X GET http://localhost:8080/api/tests-evaluation/1
```

#### Create a new test
```bash
curl -X POST http://localhost:8080/api/tests-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "idCandidature": 5,
    "typeTest": "Technique",
    "nomTest": "Test JavaScript",
    "description": "Test des compétences en JavaScript",
    "dateTest": "2025-11-01",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "dureeMinutes": 120,
    "scoreRequis": 70.00,
    "evaluateur": "Marie Martin"
  }'
```

#### Update a test
```bash
curl -X PUT http://localhost:8080/api/tests-evaluation/1 \
  -H "Content-Type: application/json" \
  -d '{
    "scoreObtenu": 85.50,
    "reussi": true,
    "commentaire": "Très bonnes compétences"
  }'
```

#### Delete a test
```bash
curl -X DELETE http://localhost:8080/api/tests-evaluation/1
```

## Notes
- All endpoints support CORS from any origin
- The API uses JSON for request and response bodies
- Database transactions are managed automatically by Spring
- Timestamps are handled in ISO 8601 format
