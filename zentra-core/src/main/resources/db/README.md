# Base de Données - Gestion des Ressources Humaines

## Description

Ce script SQL implémente un système complet de gestion des ressources humaines pour une entreprise, couvrant le processus de recrutement et la gestion des employés.

## Structure de la Base de Données

### 1. Tables de Référence

#### `departement`
Gère les départements de l'entreprise.
- Champs principaux : nom, description, budget annuel
- Suivi : date de création, statut actif

#### `poste`
Définit les postes disponibles dans l'entreprise.
- Informations : titre, description, salaires min/max
- Critères : niveau d'expérience, diplôme, compétences requises
- Lié à un département

### 2. Tables du Processus de Recrutement

Le processus de recrutement suit les 10 étapes définies :

#### `annonce_emploi`
Gère les annonces de recrutement publiées.
- Informations : titre, description, type de contrat, lieu
- Gestion : dates de publication/clôture, nombre de postes
- Statuts : Ouverte, Fermée, Suspendue, Pourvue

#### `candidat`
Enregistre les informations des candidats.
- Données personnelles : nom, prénom, contact, adresse
- Profil : âge (calculé automatiquement), formation, expérience
- Documents : CV, lettre de motivation
- Validation email avec contrainte de format

#### `candidature`
Lie un candidat à une annonce d'emploi.
- Suivi : date, statut, score initial
- Statuts : Reçue, En cours d'examen, Présélectionnée, Acceptée, Refusée

#### `test_evaluation`
Enregistre les tests et évaluations des candidats.
- Types : Technique, Psychotechnique, Langue, Personnalité, Cas pratique
- Scoring : score obtenu vs score requis
- Durée maximale : 8 heures

#### `entretien`
Planifie et enregistre les entretiens.
- Types : Téléphonique, Visio, Présentiel, Panel, Technique, RH
- Détails : date, heure, durée, lieu, intervieweurs
- Statuts : Planifié, Confirmé, En cours, Terminé, Annulé, Reporté

#### `resultat_evaluation`
Scoring détaillé des candidats (système d'évaluation).
- Critères multiples (sur 100) :
  - Compétences techniques
  - Expérience
  - Formation
  - Soft skills
  - Motivation
  - Adéquation au poste
- Score total et décision finale
- Points forts et points d'amélioration

### 3. Tables de Gestion des Employés

#### `employe`
Gère les employés de l'entreprise (transformation Candidat → Employé).
- Lien optionnel avec le candidat d'origine
- Matricule unique
- Informations professionnelles : poste, département, contrat
- Salaire et devise
- Ancienneté calculée automatiquement
- Informations administratives : numéro de sécurité sociale, compte bancaire

#### `contrat_travail`
Enregistre les contrats de travail.
- Types : Essai, CDI, CDD, Stage, Alternance
- **Période d'essai** : maximum 6 mois, renouvelable une fois
- Conditions : salaire, primes, heures, congés
- Statuts : Actif, Terminé, Suspendu, Annulé

#### `affiliation_sociale`
Gère les affiliations sociales et administratives (étape 10 du processus).
- Types : Sécurité Sociale, Mutuelle, Retraite, Prévoyance, Chômage
- Informations : organisme, numéro d'affiliation, dates
- Cotisations mensuelles

## Critères VAMPIRE Implémentés

Le système respecte tous les critères VAMPIRE pour la qualité des données :

### V - Valeurs
- Champs numériques typés : `DECIMAL` pour salaires, scores, budgets
- Contraintes `CHECK` pour valider les plages de valeurs (0-100 pour les scores)

### A - Appartenance
- Champs `statut` pour déterminer l'appartenance (Actif, Membre, etc.)
- Booléens pour les états binaires (`actif`, `essai_renouvelable`)

### M - Mesure
- Champs `DATE` et `TIMESTAMP` pour le suivi temporel
- Calculs automatiques : âge, ancienneté avec `GENERATED ALWAYS AS`
- Durées en minutes pour précision

### P - Présentation
- Structure claire et normalisée
- Vues SQL pour le reporting (`v_statistiques_recrutement`, `v_employes_actifs`, `v_contrats_essai`)
- Index pour optimisation des performances
- Commentaires sur les tables

### I - Irrégularité
- Contraintes `CHECK` complexes pour valider la cohérence :
  - Dates de fin après dates de début
  - Salaire max >= salaire min
  - Validation format email avec regex
  - Contraintes `UNIQUE` pour éviter les doublons

### R - Réalisme
- Limites réalistes sur les valeurs :
  - Salaires < 1,000,000
  - Âge minimum 16 ans
  - Durée essai max 6 mois
  - Heures hebdomadaires max 48h
  - Années de diplôme entre 1950 et aujourd'hui

### E - Existence
- Clés primaires `SERIAL` auto-incrémentées
- Contraintes `NOT NULL` sur champs essentiels
- Contraintes `UNIQUE` pour garantir l'unicité (email, matricule)
- Clés étrangères avec `REFERENCES`

## Vues Disponibles

### `v_statistiques_recrutement`
Statistiques par poste et département :
- Nombre de candidatures
- Nombre de présélectionnés
- Nombre d'acceptés
- Score moyen

### `v_employes_actifs`
Liste des employés actifs avec informations complètes :
- Identification et contact
- Poste et département
- Ancienneté et type de contrat
- Salaire

### `v_contrats_essai`
Suivi des contrats d'essai en cours :
- Employé et poste
- Dates de début et fin
- Statut de renouvellement

## Utilisation

### Installation

```bash
# Se connecter à PostgreSQL
psql -U username -d database_name

# Exécuter le script
\i zentra-core/src/main/resources/db/schema_hr.sql
```

### Données de Démonstration

Le script inclut des données de démonstration :
- 5 départements (RH, Informatique, Marketing, Finance, Production)
- 5 postes types

### Exemple de Flux : Recrutement Complet

```sql
-- 1. Créer une annonce
INSERT INTO annonce_emploi (id_poste, titre_annonce, description, type_contrat, lieu_travail)
VALUES (1, 'Développeur Full Stack Junior', 'Recherche développeur...', 'CDI', 'Paris');

-- 2. Enregistrer un candidat
INSERT INTO candidat (nom, prenom, email, date_naissance, annees_experience, cv_fichier)
VALUES ('Dupont', 'Jean', 'jean.dupont@email.com', '1995-05-15', 3, 'cv_jean_dupont.pdf');

-- 3. Créer une candidature
INSERT INTO candidature (id_candidat, id_annonce, score_initial, statut)
VALUES (1, 1, 75.00, 'Présélectionnée');

-- 4. Planifier un entretien
INSERT INTO entretien (id_candidature, type_entretien, date_entretien, heure_debut, intervieweurs)
VALUES (1, 'Technique', '2025-11-01', '14:00', 'Marie Martin, Tech Lead');

-- 5. Évaluer le candidat
INSERT INTO resultat_evaluation (id_candidature, evaluateur, score_competences_techniques, 
    score_experience, score_motivation, score_total, decision)
VALUES (1, 'Marie Martin', 80, 70, 90, 80, 'Recommandé');

-- 6. Transformer en employé
INSERT INTO employe (id_candidat, matricule, nom, prenom, email_professionnel, 
    date_naissance, id_poste, id_departement, date_embauche, type_contrat, salaire_base)
VALUES (1, 'EMP2025001', 'Dupont', 'Jean', 'jean.dupont@entreprise.com', 
    '1995-05-15', 1, 2, '2025-11-15', 'CDI', 40000);

-- 7. Créer le contrat d'essai
INSERT INTO contrat_travail (id_employe, type_contrat, numero_contrat, date_debut, 
    duree_essai_mois, essai_renouvelable, salaire_brut, heures_hebdomadaires)
VALUES (1, 'Essai', 'CONT2025001', '2025-11-15', 3, TRUE, 40000, 35);

-- 8. Affiliations sociales
INSERT INTO affiliation_sociale (id_employe, type_affiliation, organisme, numero_affiliation)
VALUES 
    (1, 'Sécurité Sociale', 'CPAM Paris', 'SS2025001'),
    (1, 'Mutuelle', 'Mutuelle Entreprise', 'MUT2025001');
```

## Index et Performances

Les index suivants sont créés pour optimiser les recherches :
- Recherche par email de candidat
- Filtrage par statut (candidats, candidatures, annonces, employés, contrats)
- Recherche par matricule employé
- Recherche par département
- Tri par date de candidature et de publication

## Contraintes et Validations

Le système implémente de nombreuses validations automatiques :
- Validation des plages de valeurs (scores 0-100)
- Cohérence des dates (fin > début)
- Format email valide
- Limites réalistes (âge, salaires, durées)
- Unicité des identifiants (email, matricule)
- Relations référentielles entre tables

## Notes Techniques

- Base de données : **PostgreSQL** (versions 12+)
- Encodage : UTF-8
- Types utilisés : `SERIAL`, `VARCHAR`, `TEXT`, `DATE`, `TIMESTAMP`, `TIME`, `DECIMAL`, `INTEGER`, `BOOLEAN`
- Colonnes calculées avec `GENERATED ALWAYS AS`
- Cascades : `DROP ... CASCADE` pour la suppression propre

## Maintenance

### Nettoyage
```sql
-- Supprimer toutes les tables (ATTENTION : perte de données)
DROP TABLE IF EXISTS affiliation_sociale CASCADE;
DROP TABLE IF EXISTS contrat_travail CASCADE;
DROP TABLE IF EXISTS resultat_evaluation CASCADE;
DROP TABLE IF EXISTS entretien CASCADE;
DROP TABLE IF EXISTS test_evaluation CASCADE;
DROP TABLE IF EXISTS candidature CASCADE;
DROP TABLE IF EXISTS candidat CASCADE;
DROP TABLE IF EXISTS annonce_emploi CASCADE;
DROP TABLE IF EXISTS employe CASCADE;
DROP TABLE IF EXISTS poste CASCADE;
DROP TABLE IF EXISTS departement CASCADE;
```

### Sauvegarde
```bash
# Export de la base
pg_dump -U username database_name > backup_hr.sql

# Restauration
psql -U username database_name < backup_hr.sql
```

## Évolutions Futures

Pistes d'amélioration :
- Gestion des congés et absences
- Planning des ressources
- Évaluation des performances
- Formation et développement des compétences
- Gestion de la paie
- Workflow d'approbation multi-niveaux
- Notifications automatiques
- Historique des modifications (audit trail)

## Support

Pour toute question ou problème, veuillez consulter la documentation PostgreSQL ou contacter l'équipe de développement.
