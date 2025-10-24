# API Module BESOIN (Gestion des Besoins en Personnel)

Ce document décrit les endpoints REST API pour le module BESOIN (Gestion des besoins en personnel).

## Base URL
```
http://localhost:8080/api/besoins
```

## Endpoints

### 1. Récupérer tous les besoins
**GET** `/api/besoins`

Récupère la liste de tous les besoins en personnel.

**Paramètres de requête optionnels:**
- `statut` (String): Filtrer par statut (ex: "Nouveau", "En cours", "Fermé")
- `departement` (String): Filtrer par département (ex: "IT", "RH", "Finance")

**Exemple de requêtes:**
```bash
# Tous les besoins
GET /api/besoins

# Besoins par statut
GET /api/besoins?statut=Nouveau

# Besoins par département
GET /api/besoins?departement=IT
```

**Réponse (200 OK):**
```json
[
  {
    "idBesoin": 1,
    "titre": "Développeur Full Stack",
    "description": "Recherche développeur Full Stack expérimenté",
    "departement": "IT",
    "nombrePostes": 2,
    "typeContrat": "CDI",
    "dateCreation": "2025-10-24",
    "dateLimite": "2025-12-31",
    "statut": "Nouveau",
    "priorite": "Haute",
    "competencesRequises": "Java, Spring Boot, React",
    "budgetAlloue": 80000.00
  }
]
```

### 2. Récupérer un besoin par ID
**GET** `/api/besoins/{id}`

Récupère les détails d'un besoin spécifique.

**Paramètres:**
- `id` (Long): Identifiant unique du besoin

**Exemple:**
```bash
GET /api/besoins/1
```

**Réponse (200 OK):**
```json
{
  "idBesoin": 1,
  "titre": "Développeur Full Stack",
  "description": "Recherche développeur Full Stack expérimenté",
  "departement": "IT",
  "nombrePostes": 2,
  "typeContrat": "CDI",
  "dateCreation": "2025-10-24",
  "dateLimite": "2025-12-31",
  "statut": "Nouveau",
  "priorite": "Haute",
  "competencesRequises": "Java, Spring Boot, React",
  "budgetAlloue": 80000.00
}
```

**Erreur (404 Not Found):**
```json
{
  "code": 404,
  "message": "Besoin non trouvé avec l'id: 999"
}
```

### 3. Créer un nouveau besoin
**POST** `/api/besoins`

Crée un nouveau besoin en personnel.

**Corps de la requête:**
```json
{
  "titre": "Développeur Full Stack",
  "description": "Recherche développeur Full Stack expérimenté",
  "departement": "IT",
  "nombrePostes": 2,
  "typeContrat": "CDI",
  "dateLimite": "2025-12-31",
  "statut": "Nouveau",
  "priorite": "Haute",
  "competencesRequises": "Java, Spring Boot, React",
  "budgetAlloue": 80000.00
}
```

**Champs obligatoires:**
- `titre` (String): Titre du besoin (non vide)
- `nombrePostes` (Integer): Nombre de postes à pourvoir (> 0)

**Réponse (201 Created):**
```json
{
  "idBesoin": 1,
  "titre": "Développeur Full Stack",
  "description": "Recherche développeur Full Stack expérimenté",
  "departement": "IT",
  "nombrePostes": 2,
  "typeContrat": "CDI",
  "dateCreation": "2025-10-24",
  "dateLimite": "2025-12-31",
  "statut": "Nouveau",
  "priorite": "Haute",
  "competencesRequises": "Java, Spring Boot, React",
  "budgetAlloue": 80000.00
}
```

**Erreur (400 Bad Request):**
```json
{
  "code": 400,
  "message": "Le titre est obligatoire"
}
```

### 4. Mettre à jour un besoin
**PUT** `/api/besoins/{id}`

Met à jour un besoin existant.

**Paramètres:**
- `id` (Long): Identifiant unique du besoin à mettre à jour

**Corps de la requête:**
```json
{
  "titre": "Développeur Full Stack Senior",
  "description": "Recherche développeur Full Stack expérimenté avec 5+ ans d'expérience",
  "departement": "IT",
  "nombrePostes": 3,
  "typeContrat": "CDI",
  "dateLimite": "2025-12-31",
  "statut": "En cours",
  "priorite": "Haute",
  "competencesRequises": "Java, Spring Boot, React, Kubernetes",
  "budgetAlloue": 90000.00
}
```

**Réponse (200 OK):**
```json
{
  "idBesoin": 1,
  "titre": "Développeur Full Stack Senior",
  "description": "Recherche développeur Full Stack expérimenté avec 5+ ans d'expérience",
  "departement": "IT",
  "nombrePostes": 3,
  "typeContrat": "CDI",
  "dateCreation": "2025-10-24",
  "dateLimite": "2025-12-31",
  "statut": "En cours",
  "priorite": "Haute",
  "competencesRequises": "Java, Spring Boot, React, Kubernetes",
  "budgetAlloue": 90000.00
}
```

**Erreur (404 Not Found):**
```json
{
  "code": 404,
  "message": "Besoin non trouvé avec l'id: 999"
}
```

### 5. Supprimer un besoin
**DELETE** `/api/besoins/{id}`

Supprime un besoin existant.

**Paramètres:**
- `id` (Long): Identifiant unique du besoin à supprimer

**Exemple:**
```bash
DELETE /api/besoins/1
```

**Réponse (204 No Content):**
Aucun contenu retourné en cas de succès.

**Erreur (404 Not Found):**
```json
{
  "code": 404,
  "message": "Besoin non trouvé avec l'id: 999"
}
```

## Codes d'état HTTP

- `200 OK`: Requête réussie (GET, PUT)
- `201 Created`: Ressource créée avec succès (POST)
- `204 No Content`: Suppression réussie (DELETE)
- `400 Bad Request`: Erreur de validation des données
- `404 Not Found`: Ressource non trouvée
- `500 Internal Server Error`: Erreur interne du serveur

## Exemples d'utilisation avec cURL

### Créer un besoin
```bash
curl -X POST http://localhost:8080/api/besoins \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Développeur Full Stack",
    "description": "Recherche développeur Full Stack expérimenté",
    "departement": "IT",
    "nombrePostes": 2,
    "typeContrat": "CDI",
    "dateLimite": "2025-12-31",
    "statut": "Nouveau",
    "priorite": "Haute",
    "competencesRequises": "Java, Spring Boot, React",
    "budgetAlloue": 80000.00
  }'
```

### Récupérer tous les besoins
```bash
curl -X GET http://localhost:8080/api/besoins
```

### Récupérer un besoin par ID
```bash
curl -X GET http://localhost:8080/api/besoins/1
```

### Mettre à jour un besoin
```bash
curl -X PUT http://localhost:8080/api/besoins/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Développeur Full Stack Senior",
    "description": "Recherche développeur Full Stack expérimenté",
    "departement": "IT",
    "nombrePostes": 3,
    "typeContrat": "CDI",
    "dateLimite": "2025-12-31",
    "statut": "En cours",
    "priorite": "Haute",
    "competencesRequises": "Java, Spring Boot, React, Kubernetes",
    "budgetAlloue": 90000.00
  }'
```

### Supprimer un besoin
```bash
curl -X DELETE http://localhost:8080/api/besoins/1
```

### Filtrer par statut
```bash
curl -X GET "http://localhost:8080/api/besoins?statut=Nouveau"
```

### Filtrer par département
```bash
curl -X GET "http://localhost:8080/api/besoins?departement=IT"
```

## Structure de la base de données

Le module BESOIN utilise la table `besoin` avec la structure suivante:

| Colonne | Type | Description |
|---------|------|-------------|
| id_besoin | SERIAL (Primary Key) | Identifiant unique auto-généré |
| titre | VARCHAR(200) | Titre du besoin (obligatoire) |
| description | TEXT | Description détaillée du besoin |
| departement | VARCHAR(100) | Département concerné |
| nombre_postes | INTEGER | Nombre de postes à pourvoir (obligatoire) |
| type_contrat | VARCHAR(50) | Type de contrat (CDI, CDD, Stage, etc.) |
| date_creation | DATE | Date de création (auto-générée) |
| date_limite | DATE | Date limite pour pourvoir le besoin |
| statut | VARCHAR(50) | Statut du besoin (Nouveau par défaut) |
| priorite | VARCHAR(20) | Priorité du besoin (Basse, Moyenne, Haute) |
| competences_requises | TEXT | Liste des compétences requises |
| budget_alloue | NUMERIC | Budget alloué pour le recrutement |

## Notes techniques

- Les endpoints sont automatiquement exposés via Spring Boot
- Le contexte de l'application est `/api` (configuré dans application.properties)
- L'API utilise le format JSON pour les requêtes et réponses
- La validation des données est effectuée côté service
- La gestion des erreurs est centralisée via GlobalExceptionHandler
- Les tests unitaires couvrent tous les cas d'usage du service
