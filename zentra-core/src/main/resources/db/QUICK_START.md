# Guide de Démarrage Rapide - Base de Données RH

## Installation et Déploiement

### Prérequis
- PostgreSQL 12 ou supérieur
- Accès à une base de données PostgreSQL

### Installation en 3 étapes

#### 1. Créer la base de données
```bash
# Se connecter à PostgreSQL
psql -U postgres

# Créer la base de données
CREATE DATABASE gestion_rh;

# Quitter psql
\q
```

#### 2. Exécuter le schéma
```bash
# Appliquer le schéma de base de données
psql -U postgres -d gestion_rh -f schema_hr.sql
```

#### 3. (Optionnel) Tester avec des données de démonstration
```bash
# Exécuter le test complet du workflow de recrutement
psql -U postgres -d gestion_rh -f test_hr_workflow.sql
```

## Utilisation

### Vérifier l'installation

```sql
-- Se connecter à la base de données
psql -U postgres -d gestion_rh

-- Lister les tables
\dt

-- Lister les vues
\dv

-- Vérifier les données de démonstration
SELECT * FROM departement;
SELECT * FROM poste;
```

### Exemples d'Utilisation

#### Consulter les candidatures en cours
```sql
SELECT 
    c.nom || ' ' || c.prenom AS candidat,
    a.titre_annonce AS poste,
    ca.statut,
    ca.score_initial,
    ca.date_candidature
FROM candidature ca
JOIN candidat c ON ca.id_candidat = c.id_candidat
JOIN annonce_emploi a ON ca.id_annonce = a.id_annonce
WHERE ca.statut IN ('Reçue', 'En cours d''examen', 'Présélectionnée')
ORDER BY ca.date_candidature DESC;
```

#### Voir les employés actifs
```sql
SELECT * FROM v_employes_actifs;
```

#### Suivre les contrats d'essai
```sql
SELECT * FROM v_contrats_essai;
```

#### Statistiques de recrutement
```sql
SELECT * FROM v_statistiques_recrutement;
```

## Processus de Recrutement Complet

### 1. Créer une annonce d'emploi
```sql
INSERT INTO annonce_emploi (
    id_poste, titre_annonce, description, type_contrat, lieu_travail
) VALUES (
    1, 'Titre du poste', 'Description...', 'CDI', 'Paris'
);
```

### 2. Enregistrer un candidat
```sql
INSERT INTO candidat (
    nom, prenom, email, date_naissance, 
    annees_experience, cv_fichier
) VALUES (
    'Nom', 'Prénom', 'email@example.com', '1990-01-01',
    5, 'cv_candidat.pdf'
);
```

### 3. Créer une candidature
```sql
INSERT INTO candidature (
    id_candidat, id_annonce, score_initial, statut
) VALUES (
    1, 1, 75.00, 'Présélectionnée'
);
```

### 4. Planifier un test
```sql
INSERT INTO test_evaluation (
    id_candidature, type_test, nom_test, 
    date_test, score_requis
) VALUES (
    1, 'Technique', 'Test JavaScript', 
    CURRENT_DATE + 7, 70.00
);
```

### 5. Planifier un entretien
```sql
INSERT INTO entretien (
    id_candidature, type_entretien, 
    date_entretien, heure_debut, intervieweurs
) VALUES (
    1, 'RH', CURRENT_DATE + 14, 
    '14:00', 'Sophie Lambert, RH Manager'
);
```

### 6. Évaluer le candidat
```sql
INSERT INTO resultat_evaluation (
    id_candidature, evaluateur, 
    score_competences_techniques, score_experience, 
    score_motivation, score_total, decision
) VALUES (
    1, 'Sophie Lambert', 
    85, 80, 90, 85, 'Recommandé'
);
```

### 7. Embaucher (créer un employé)
```sql
INSERT INTO employe (
    matricule, nom, prenom, email_professionnel,
    date_naissance, id_poste, id_departement,
    date_embauche, type_contrat, salaire_base
) VALUES (
    'EMP2025001', 'Nom', 'Prénom', 'prenom.nom@entreprise.com',
    '1990-01-01', 1, 2,
    CURRENT_DATE, 'CDI', 40000
);
```

### 8. Créer le contrat d'essai
```sql
INSERT INTO contrat_travail (
    id_employe, type_contrat, numero_contrat,
    date_debut, duree_essai_mois, essai_renouvelable,
    salaire_brut, heures_hebdomadaires
) VALUES (
    1, 'Essai', 'CONT2025001',
    CURRENT_DATE, 3, TRUE,
    40000, 35
);
```

### 9. Créer les affiliations sociales
```sql
INSERT INTO affiliation_sociale (
    id_employe, type_affiliation, organisme, numero_affiliation
) VALUES 
    (1, 'Sécurité Sociale', 'CPAM', 'SS2025001'),
    (1, 'Mutuelle', 'Mutuelle Entreprise', 'MUT2025001'),
    (1, 'Retraite', 'Caisse Retraite', 'RET2025001');
```

## Configuration Spring Boot

### application.properties (pour Spring Boot)
```properties
spring.application.name=zentra-core

# Configuration PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_rh
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Initialisation de la base de données
spring.sql.init.mode=never
```

### application.yml (alternative)
```yaml
spring:
  application:
    name: zentra-core
  datasource:
    url: jdbc:postgresql://localhost:5432/gestion_rh
    username: postgres
    password: votre_mot_de_passe
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

## Maintenance

### Sauvegarde
```bash
# Sauvegarder toute la base
pg_dump -U postgres gestion_rh > backup_$(date +%Y%m%d).sql

# Sauvegarder uniquement le schéma
pg_dump -U postgres --schema-only gestion_rh > schema_backup.sql

# Sauvegarder uniquement les données
pg_dump -U postgres --data-only gestion_rh > data_backup.sql
```

### Restauration
```bash
# Restaurer depuis une sauvegarde
psql -U postgres -d gestion_rh < backup_20251024.sql
```

### Réinitialisation complète
```bash
# ATTENTION : Supprime toutes les données !
psql -U postgres -d gestion_rh -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
psql -U postgres -d gestion_rh -f schema_hr.sql
```

## Support et Documentation

- Voir `README.md` pour la documentation complète
- Voir `schema_hr.sql` pour le schéma de base de données
- Voir `test_hr_workflow.sql` pour des exemples d'utilisation

## Contacts

Pour toute question ou problème, veuillez contacter l'équipe de développement.
