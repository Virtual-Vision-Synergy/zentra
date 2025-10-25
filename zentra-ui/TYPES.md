# Documentation des Types de Données

## DTOs (Data Transfer Objects)

### CandidateMinInfoDto
Informations minimales du candidat retournées après l'authentification.

```typescript
{
  id: number;              // ID unique du candidat
  firstName: string;        // Prénom
  lastName: string;         // Nom
  email: string;           // Email
  applicationId: number;    // ID de la candidature
}
```

### ChoiceDto
Représente un choix de réponse pour une question.

```typescript
{
  id: number;              // ID unique du choix
  libelle: string;         // Texte du choix
  correct?: boolean;       // Si c'est la bonne réponse (optionnel)
  questionId: number;      // ID de la question parente
}
```

### QuestionDto
Représente une question du QCM avec ses choix.

```typescript
{
  id: number;              // ID unique de la question
  libelle: string;         // Texte de la question
  required: boolean;       // Si la question est obligatoire
  score: number;           // Nombre de points
  qcmId: number;          // ID du QCM parent
  choices: ChoiceDto[];    // Liste des choix possibles
}
```

### QcmDto
Représente le QCM complet avec toutes ses questions.

```typescript
{
  id: number;              // ID unique du QCM
  title: string;           // Titre du QCM
  description: string;     // Description
  durationMinutes: number; // Durée en minutes
  totalScore: number;      // Score total possible
  requiredScore: number;   // Score minimum requis
  questions: QuestionDto[]; // Liste des questions
}
```

### AnswerDto
Représente la réponse d'un candidat à une question.

```typescript
{
  choiceId: number;        // ID du choix sélectionné
}
```

### AttemptDto
Représente la tentative complète avec toutes les réponses.

```typescript
{
  answers: AnswerDto[];    // Liste de toutes les réponses
}
```

## Exemples

### Exemple de réponse d'authentification
```json
{
  "id": 1,
  "firstName": "Jean",
  "lastName": "Dupont",
  "email": "jean.dupont@example.com",
  "applicationId": 42
}
```

### Exemple de QCM
```json
{
  "id": 1,
  "title": "Test JavaScript",
  "description": "Évaluation des connaissances en JavaScript",
  "durationMinutes": 30,
  "totalScore": 100,
  "requiredScore": 60,
  "questions": [
    {
      "id": 1,
      "libelle": "Quel est le type de 'null' en JavaScript?",
      "required": true,
      "score": 10,
      "qcmId": 1,
      "choices": [
        {
          "id": 1,
          "libelle": "object",
          "questionId": 1
        },
        {
          "id": 2,
          "libelle": "null",
          "questionId": 1
        },
        {
          "id": 3,
          "libelle": "undefined",
          "questionId": 1
        }
      ]
    }
  ]
}
```

### Exemple de soumission
```json
{
  "answers": [
    { "choiceId": 1 },
    { "choiceId": 5 },
    { "choiceId": 9 }
  ]
}
```

## Notes

- Les questions peuvent être **obligatoires** (`required: true`) ou **optionnelles** (`required: false`)
- Le score total est la somme de tous les scores des questions
- Un candidat doit obtenir au minimum `requiredScore` points pour réussir
- Chaque réponse doit correspondre à un `choiceId` valide

