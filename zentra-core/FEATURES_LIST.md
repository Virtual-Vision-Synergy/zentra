# 📋 Liste Complète des Fonctionnalités - Module Besoins en Personnel

## Vue d'ensemble

Le module de **Gestion des Besoins en Personnel** offre une API REST complète pour gérer les demandes de recrutement de l'entreprise.

---

## 🔌 Endpoints API (9 endpoints)

### 1️⃣ Créer un besoin en personnel

**Endpoint** : `POST /api/staffing-needs`

**Fonctionnalité** : Créer une nouvelle demande de recrutement

**Validations** :
- ✅ Titre obligatoire et non vide
- ✅ Nombre de postes > 0
- ✅ Statut par défaut : "Open"
- ✅ Département doit exister
- ✅ Poste doit exister
- ✅ Employé demandeur doit exister (si fourni)

**Cas d'usage** :
- Département RH crée un nouveau besoin de recrutement
- Manager demande du personnel supplémentaire
- Planification des ressources

---

### 2️⃣ Modifier un besoin en personnel

**Endpoint** : `PUT /api/staffing-needs/{id}`

**Fonctionnalité** : Mettre à jour un besoin existant

**Validations** :
- ✅ ID requis
- ✅ Besoin doit exister
- ✅ Toutes les validations de création

**Cas d'usage** :
- Ajuster le nombre de postes requis
- Changer la priorité
- Mettre à jour le statut
- Modifier le budget alloué

---

### 3️⃣ Obtenir un besoin par ID

**Endpoint** : `GET /api/staffing-needs/{id}`

**Fonctionnalité** : Récupérer les détails d'un besoin spécifique

**Informations retournées** :
- Toutes les informations du besoin
- Nom du département
- Titre du poste
- Nom de l'employé demandeur
- Timestamps de création et modification

**Cas d'usage** :
- Consulter les détails d'une demande
- Afficher un besoin dans l'interface
- Préparer une modification

---

### 4️⃣ Lister tous les besoins

**Endpoint** : `GET /api/staffing-needs`

**Fonctionnalité** : Obtenir la liste complète de tous les besoins

**Cas d'usage** :
- Dashboard RH
- Vue d'ensemble des besoins
- Export de données
- Reporting

---

### 5️⃣ Filtrer par département

**Endpoint** : `GET /api/staffing-needs/department/{departmentId}`

**Fonctionnalité** : Obtenir tous les besoins d'un département spécifique

**Cas d'usage** :
- Vue par département
- Manager consulte ses besoins
- Analyse budgétaire par département
- Planning RH départemental

---

### 6️⃣ Filtrer par poste

**Endpoint** : `GET /api/staffing-needs/job/{jobId}`

**Fonctionnalité** : Obtenir tous les besoins pour un poste donné

**Cas d'usage** :
- Analyser la demande pour un type de poste
- Planifier les recrutements par compétence
- Statistiques par fonction

---

### 7️⃣ Filtrer par statut

**Endpoint** : `GET /api/staffing-needs/status/{status}`

**Statuts disponibles** :
- `Open` : Besoins ouverts, non traités
- `In Progress` : Recrutement en cours
- `Fulfilled` : Besoin satisfait
- `Cancelled` : Besoin annulé

**Cas d'usage** :
- Voir les besoins actifs
- Suivre les recrutements en cours
- Historique des besoins satisfaits

---

### 8️⃣ Filtrer par priorité

**Endpoint** : `GET /api/staffing-needs/priority/{priority}`

**Priorités disponibles** :
- `High` : Urgence élevée
- `Medium` : Priorité moyenne
- `Low` : Priorité basse

**Cas d'usage** :
- Traiter les urgences en premier
- Planification basée sur les priorités
- Allocation des ressources RH

---

### 9️⃣ Supprimer un besoin

**Endpoint** : `DELETE /api/staffing-needs/{id}`

**Fonctionnalité** : Supprimer définitivement un besoin

**Validations** :
- ✅ Besoin doit exister

**Cas d'usage** :
- Annulation d'une demande
- Nettoyage des données
- Correction d'erreurs

---

## 📊 Modèle de données

### Champs disponibles

| Champ | Type | Description | Requis | Validations |
|-------|------|-------------|--------|-------------|
| **id** | Long | Identifiant unique | Auto | - |
| **title** | String | Titre du besoin | ✅ Oui | Non vide, max 200 |
| **description** | String | Description détaillée | Non | Texte long |
| **numberOfPositions** | Integer | Nombre de postes | ✅ Oui | > 0 |
| **priority** | String | Priorité | Non | High/Medium/Low |
| **status** | String | Statut actuel | ✅ Oui | Open/In Progress/Fulfilled/Cancelled |
| **requiredStartDate** | LocalDate | Date de début souhaitée | Non | Format date |
| **budgetAllocated** | Double | Budget alloué | Non | >= 0 |
| **justification** | String | Justification du besoin | Non | Texte long |
| **departmentId** | Long | ID du département | ✅ Oui | Doit exister |
| **jobId** | Long | ID du poste | ✅ Oui | Doit exister |
| **requestedById** | Long | ID de l'employé demandeur | Non | Doit exister si fourni |
| **createdAt** | DateTime | Date de création | Auto | - |
| **updatedAt** | DateTime | Date de modification | Auto | - |

---

## 🔍 Fonctionnalités métier

### 1. Validation des données

**Règles implémentées** :
- ✅ Titre non vide
- ✅ Nombre de positions strictement positif
- ✅ Statut obligatoire
- ✅ Vérification de l'existence du département
- ✅ Vérification de l'existence du poste
- ✅ Vérification de l'existence de l'employé (si fourni)

### 2. Relations avec les autres entités

**Department** (Département)
- Relation : Many-to-One
- Un besoin appartient à un département
- Un département peut avoir plusieurs besoins

**Job** (Poste)
- Relation : Many-to-One
- Un besoin concerne un poste spécifique
- Un poste peut avoir plusieurs besoins

**Employee** (Employé demandeur)
- Relation : Many-to-One (optionnelle)
- Un besoin peut être demandé par un employé
- Un employé peut demander plusieurs besoins

### 3. Audit automatique

**Timestamps** :
- `createdAt` : Date/heure de création (automatique)
- `updatedAt` : Date/heure de dernière modification (automatique)

**Traçabilité** :
- `requestedBy` : Employé ayant fait la demande

---

## 🎯 Cas d'usage détaillés

### Scénario 1 : Nouveau besoin urgent
```
1. Manager identifie un besoin urgent
2. Crée un besoin avec priorité "High"
3. Statut automatique "Open"
4. RH reçoit notification (futur)
5. RH traite en priorité
```

### Scénario 2 : Suivi d'un recrutement
```
1. RH consulte les besoins "Open"
2. Lance un processus de recrutement
3. Change le statut à "In Progress"
4. Publie une annonce
5. Une fois recruté, statut "Fulfilled"
```

### Scénario 3 : Planification budgétaire
```
1. Direction consulte tous les besoins
2. Filtre par département
3. Totalise les budgets alloués
4. Prend décisions d'allocation
5. Ajuste les priorités
```

### Scénario 4 : Analyse RH
```
1. Consulte besoins par statut
2. Identifie les besoins en retard
3. Analyse par priorité
4. Réaffecte les ressources
5. Met à jour les statuts
```

### Scénario 5 : Reporting
```
1. Export tous les besoins
2. Filtre par période (createdAt)
3. Statistiques par département
4. Taux de satisfaction des besoins
5. Génération de rapports
```

---

## 🔐 Sécurité (futures implémentations)

### Permissions recommandées

**ROLE_HR_MANAGER**
- Créer, modifier, supprimer tous les besoins
- Voir tous les besoins

**ROLE_DEPARTMENT_MANAGER**
- Créer des besoins pour son département
- Modifier ses besoins
- Voir ses besoins

**ROLE_EMPLOYEE**
- Voir les besoins de son département
- Lecture seule

**ROLE_ADMIN**
- Accès complet

---

## 📈 Statistiques disponibles (via requêtes)

### 1. Besoins ouverts par priorité
```
GET /api/staffing-needs/priority/High + status filter
```

### 2. Besoins par département
```
GET /api/staffing-needs/department/{id}
```

### 3. Total des postes à pourvoir
```
Somme de numberOfPositions pour status=Open
```

### 4. Budget total alloué
```
Somme de budgetAllocated pour tous les besoins
```

---

## 🚀 Performances

### Optimisations implémentées
- ✅ Lazy loading des relations
- ✅ Transactions automatiques
- ✅ Index sur les foreign keys
- ✅ Requêtes optimisées JPA

### Temps de réponse attendus
- GET single : < 100ms
- GET list : < 500ms
- POST/PUT : < 200ms
- DELETE : < 100ms

---

## 🔄 Workflow typique

```
┌─────────────────┐
│   Besoin créé   │ (Status: Open, Priority: High/Medium/Low)
│   (POST)        │
└────────┬────────┘
         │
         v
┌─────────────────┐
│ RH consulte     │ (GET /status/Open)
└────────┬────────┘
         │
         v
┌─────────────────┐
│ Processus lancé │ (PUT - Status: In Progress)
└────────┬────────┘
         │
         v
┌─────────────────┐
│ Recrutement OK  │ (PUT - Status: Fulfilled)
└─────────────────┘
```

---

## 📋 Checklist d'utilisation

### Pour créer un besoin
- [ ] Vérifier que le département existe
- [ ] Vérifier que le poste existe
- [ ] Définir le titre
- [ ] Indiquer le nombre de postes
- [ ] Choisir la priorité
- [ ] Justifier le besoin
- [ ] Estimer le budget

### Pour suivre les besoins
- [ ] Consulter régulièrement les besoins "Open"
- [ ] Mettre à jour les statuts
- [ ] Ajuster les priorités si nécessaire
- [ ] Documenter les actions

---

## 🎨 Intégrations futures possibles

- 📧 Notifications par email
- 📊 Dashboard analytics
- 📄 Génération de PDF
- 🔔 Alertes automatiques
- 📅 Intégration calendrier
- 💬 Système de commentaires
- 📈 Graphiques et statistiques
- 🔗 Lien avec module Publication

---

**📌 Cette API est prête à l'emploi et peut être étendue selon les besoins du projet.**
