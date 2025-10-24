# Module BESOIN - Gestion des Besoins en Personnel

## Vue d'ensemble

Le module BESOIN permet de gérer les besoins en personnel de l'entreprise. Il offre une API REST complète pour créer, lire, mettre à jour et supprimer (CRUD) les besoins de recrutement.

## Structure du module

```
besoin/
├── controller/
│   └── BesoinController.java      # Contrôleur REST exposant les endpoints
├── dto/
│   ├── BesoinRequestDTO.java      # DTO pour les requêtes entrantes
│   └── BesoinResponseDTO.java     # DTO pour les réponses
├── entity/
│   └── Besoin.java                # Entité JPA représentant la table besoin
├── repository/
│   └── BesoinRepository.java      # Interface de persistance Spring Data JPA
└── service/
    └── BesoinService.java         # Logique métier et validation
```

## Fonctionnalités

### Endpoints REST (via BesoinController)

- **GET /api/besoins** : Récupère tous les besoins (avec filtres optionnels)
- **GET /api/besoins/{id}** : Récupère un besoin spécifique
- **POST /api/besoins** : Crée un nouveau besoin
- **PUT /api/besoins/{id}** : Met à jour un besoin existant
- **DELETE /api/besoins/{id}** : Supprime un besoin

### Filtres disponibles

- Filtrage par **statut** : `?statut=Nouveau`
- Filtrage par **département** : `?departement=IT`

## Modèle de données

### Entité Besoin

| Attribut | Type | Description |
|----------|------|-------------|
| idBesoin | Long | Identifiant unique (auto-généré) |
| titre | String | Titre du besoin (obligatoire) |
| description | String | Description détaillée |
| departement | String | Département concerné |
| nombrePostes | Integer | Nombre de postes (obligatoire, > 0) |
| typeContrat | String | Type de contrat (CDI, CDD, etc.) |
| dateCreation | LocalDate | Date de création (auto-générée) |
| dateLimite | LocalDate | Date limite |
| statut | String | Statut ("Nouveau" par défaut) |
| priorite | String | Priorité du besoin |
| competencesRequises | String | Compétences requises |
| budgetAlloue | Double | Budget alloué |

## Validation

Le service BesoinService effectue les validations suivantes :

- **Titre obligatoire** : Le titre ne peut pas être null ou vide
- **Nombre de postes** : Doit être supérieur à 0
- **Existence** : Vérifie l'existence lors de la mise à jour ou suppression

## Gestion des erreurs

Les erreurs sont gérées par le `GlobalExceptionHandler` :

- **EntityNotFoundException** : Retourne 404 Not Found
- **IllegalArgumentException** : Retourne 400 Bad Request
- **Exception** : Retourne 500 Internal Server Error

## Tests

Le module inclut des tests unitaires complets (BesoinServiceTest) :

- Test de récupération de tous les besoins
- Test de récupération par ID
- Test de création avec validation
- Test de mise à jour
- Test de suppression
- Test de filtrage par statut et département
- Test de gestion des erreurs

**Résultat des tests** : 12/12 tests passés ✅

## Utilisation

### Exemple de création d'un besoin

```java
BesoinRequestDTO request = new BesoinRequestDTO();
request.setTitre("Développeur Full Stack");
request.setDescription("Recherche développeur expérimenté");
request.setDepartement("IT");
request.setNombrePostes(2);
request.setTypeContrat("CDI");
request.setStatut("Nouveau");
request.setPriorite("Haute");

BesoinResponseDTO response = besoinService.createBesoin(request);
```

### Exemple avec cURL

```bash
curl -X POST http://localhost:8080/api/besoins \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Développeur Full Stack",
    "departement": "IT",
    "nombrePostes": 2,
    "typeContrat": "CDI"
  }'
```

## Configuration requise

- Java 17+
- Spring Boot 3.5.7
- PostgreSQL
- Maven

## Démarrage

1. Assurez-vous que PostgreSQL est en cours d'exécution
2. Configurez les paramètres de connexion dans `application.properties`
3. Lancez l'application : `./mvnw spring-boot:run`
4. L'API sera disponible sur `http://localhost:8080/api`

## Documentation complète

Pour plus de détails sur l'API, consultez [BESOIN_API.md](../../../../../../../BESOIN_API.md) à la racine du projet zentra-core.

## Sécurité

✅ Aucune vulnérabilité détectée par CodeQL

## Auteur

Module développé pour le projet Zentra - Gestion des Ressources Humaines
