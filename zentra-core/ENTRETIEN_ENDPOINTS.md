# ENTRETIEN Module - REST API Endpoints

## Overview
This document describes the REST API endpoints for the ENTRETIEN (Interview) module in zentra-core.

## Base URL
All endpoints are prefixed with the application context path: `/api/entretiens`

## Endpoints

### 1. Get All Entretiens
**GET** `/api/entretiens`

Returns a list of all interviews.

**Response:** `200 OK`
```json
[
  {
    "idEntretien": 1,
    "idCandidature": 1,
    "typeEntretien": "Téléphonique",
    "dateEntretien": "2025-10-25",
    "heureDebut": "10:00:00",
    "heureFin": "11:00:00",
    "dureeMinutes": 60,
    "lieu": "Remote",
    "intervieweurs": "John Doe, Jane Smith",
    "statut": "Planifié",
    "compteRendu": null,
    "dateCreation": "2025-10-24T12:00:00"
  }
]
```

### 2. Get Entretien by ID
**GET** `/api/entretiens/{id}`

Returns a specific interview by its ID.

**Parameters:**
- `id` (path): The interview ID

**Response:** `200 OK` or `404 Not Found`

### 3. Get Entretiens by Candidature
**GET** `/api/entretiens/candidature/{idCandidature}`

Returns all interviews for a specific candidature.

**Parameters:**
- `idCandidature` (path): The candidature ID

**Response:** `200 OK`

### 4. Get Entretiens by Statut
**GET** `/api/entretiens/statut/{statut}`

Returns all interviews with a specific status.

**Parameters:**
- `statut` (path): The status (e.g., "Planifié", "Confirmé", "En cours", "Terminé", "Annulé", "Reporté")

**Response:** `200 OK`

### 5. Get Entretiens by Date
**GET** `/api/entretiens/date/{date}`

Returns all interviews scheduled for a specific date.

**Parameters:**
- `date` (path): The date in ISO format (YYYY-MM-DD)

**Response:** `200 OK`

### 6. Get Entretiens by Date Range
**GET** `/api/entretiens/date-range?startDate={startDate}&endDate={endDate}`

Returns all interviews within a date range.

**Parameters:**
- `startDate` (query): Start date in ISO format (YYYY-MM-DD)
- `endDate` (query): End date in ISO format (YYYY-MM-DD)

**Response:** `200 OK`

### 7. Get Entretiens by Type
**GET** `/api/entretiens/type/{typeEntretien}`

Returns all interviews of a specific type.

**Parameters:**
- `typeEntretien` (path): The type (e.g., "Téléphonique", "Visio", "Présentiel", "Panel", "Technique", "RH")

**Response:** `200 OK`

### 8. Create Entretien
**POST** `/api/entretiens`

Creates a new interview.

**Request Body:**
```json
{
  "idCandidature": 1,
  "typeEntretien": "Téléphonique",
  "dateEntretien": "2025-10-25",
  "heureDebut": "10:00:00",
  "heureFin": "11:00:00",
  "dureeMinutes": 60,
  "lieu": "Remote",
  "intervieweurs": "John Doe, Jane Smith",
  "statut": "Planifié",
  "compteRendu": null
}
```

**Response:** `201 Created`

**Validation Rules:**
- `idCandidature`: Required
- `typeEntretien`: Required, non-empty
- `dateEntretien`: Required
- `heureDebut`: Required
- `intervieweurs`: Required, non-empty
- `heureFin`: Must be after `heureDebut` if provided
- `dureeMinutes`: Must be between 1 and 240 minutes if provided

### 9. Update Entretien
**PUT** `/api/entretiens/{id}`

Updates an existing interview.

**Parameters:**
- `id` (path): The interview ID

**Request Body:** Same as Create Entretien

**Response:** `200 OK` or `404 Not Found`

### 10. Delete Entretien
**DELETE** `/api/entretiens/{id}`

Deletes an interview.

**Parameters:**
- `id` (path): The interview ID

**Response:** `204 No Content` or `404 Not Found`

## Error Responses

### 400 Bad Request
Returned when validation fails.

```json
{
  "code": 400,
  "message": "Heure fin must be after heure début"
}
```

### 404 Not Found
Returned when an entity is not found.

```json
{
  "code": 404,
  "message": "Entretien not found with id: 123"
}
```

### 500 Internal Server Error
Returned for unexpected server errors.

```json
{
  "code": 500,
  "message": "Internal server error"
}
```

## Database Schema Reference

The `entretien` table structure:
- `id_entretien` (SERIAL PRIMARY KEY)
- `id_candidature` (INTEGER NOT NULL) - Foreign key to candidature
- `type_entretien` (VARCHAR(100) NOT NULL) - Interview type
- `date_entretien` (DATE NOT NULL) - Interview date
- `heure_debut` (TIME NOT NULL) - Start time
- `heure_fin` (TIME) - End time
- `duree_minutes` (INTEGER) - Duration in minutes (1-240)
- `lieu` (VARCHAR(200)) - Location
- `intervieweurs` (TEXT NOT NULL) - List of interviewers
- `statut` (VARCHAR(50) NOT NULL) - Status (default: "Planifié")
- `compte_rendu` (TEXT) - Interview report
- `date_creation` (TIMESTAMP NOT NULL) - Creation timestamp

## Usage Examples

### Create an interview
```bash
curl -X POST http://localhost:8080/api/entretiens \
  -H "Content-Type: application/json" \
  -d '{
    "idCandidature": 1,
    "typeEntretien": "Présentiel",
    "dateEntretien": "2025-10-25",
    "heureDebut": "14:00:00",
    "heureFin": "15:30:00",
    "dureeMinutes": 90,
    "lieu": "Bureau Paris",
    "intervieweurs": "Marie Dubois, Pierre Martin",
    "statut": "Planifié"
  }'
```

### Get all interviews for a candidature
```bash
curl http://localhost:8080/api/entretiens/candidature/1
```

### Get interviews by date range
```bash
curl "http://localhost:8080/api/entretiens/date-range?startDate=2025-10-20&endDate=2025-10-30"
```

### Update interview status
```bash
curl -X PUT http://localhost:8080/api/entretiens/1 \
  -H "Content-Type: application/json" \
  -d '{
    "idCandidature": 1,
    "typeEntretien": "Présentiel",
    "dateEntretien": "2025-10-25",
    "heureDebut": "14:00:00",
    "heureFin": "15:30:00",
    "dureeMinutes": 90,
    "lieu": "Bureau Paris",
    "intervieweurs": "Marie Dubois, Pierre Martin",
    "statut": "Terminé",
    "compteRendu": "Le candidat a bien performé..."
  }'
```
