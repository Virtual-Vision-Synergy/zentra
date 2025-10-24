-- ==========================================
-- SCRIPT SQL - GESTION DES RESSOURCES HUMAINES
-- Système de gestion pour entreprise
-- Base de données PostgreSQL
-- ==========================================

-- Suppression des tables existantes (dans l'ordre inverse des dépendances)
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

-- ==========================================
-- TABLES DE RÉFÉRENCE
-- ==========================================

-- Table Département
CREATE TABLE departement (
    id_departement SERIAL PRIMARY KEY,
    nom_departement VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    budget_annuel DECIMAL(15, 2) CHECK (budget_annuel >= 0), -- V: Valeurs, R: Réalisme
    date_creation DATE NOT NULL DEFAULT CURRENT_DATE,
    actif BOOLEAN NOT NULL DEFAULT TRUE -- A: Appartenance
);

-- Table Poste
CREATE TABLE poste (
    id_poste SERIAL PRIMARY KEY,
    titre_poste VARCHAR(100) NOT NULL,
    id_departement INTEGER NOT NULL REFERENCES departement(id_departement),
    description_poste TEXT,
    salaire_min DECIMAL(10, 2) NOT NULL CHECK (salaire_min > 0), -- V: Valeurs, R: Réalisme
    salaire_max DECIMAL(10, 2) NOT NULL CHECK (salaire_max >= salaire_min), -- V: Valeurs, R: Réalisme, I: Irrégularité
    niveau_experience_requis VARCHAR(50) CHECK (niveau_experience_requis IN ('Junior', 'Intermédiaire', 'Senior', 'Expert')),
    diplome_requis VARCHAR(100),
    competences_requises TEXT,
    date_creation DATE NOT NULL DEFAULT CURRENT_DATE,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_salaire_realiste CHECK (salaire_min <= 1000000 AND salaire_max <= 1000000) -- R: Réalisme
);

-- ==========================================
-- TABLES PROCESSUS DE RECRUTEMENT
-- ==========================================

-- Table Annonce d'emploi
CREATE TABLE annonce_emploi (
    id_annonce SERIAL PRIMARY KEY,
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    titre_annonce VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    type_contrat VARCHAR(50) CHECK (type_contrat IN ('CDI', 'CDD', 'Stage', 'Alternance', 'Interim')),
    lieu_travail VARCHAR(100),
    date_publication DATE NOT NULL DEFAULT CURRENT_DATE, -- M: Mesure
    date_cloture DATE,
    nombre_postes INTEGER NOT NULL DEFAULT 1 CHECK (nombre_postes > 0), -- V: Valeurs
    statut VARCHAR(50) NOT NULL DEFAULT 'Ouverte' CHECK (statut IN ('Ouverte', 'Fermée', 'Suspendue', 'Pourvue')),
    canaux_publication TEXT, -- P: Présentation
    CONSTRAINT chk_dates_annonce CHECK (date_cloture IS NULL OR date_cloture >= date_publication) -- I: Irrégularité
);

-- Table Candidat
CREATE TABLE candidat (
    id_candidat SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE, -- E: Existence
    telephone VARCHAR(20),
    date_naissance DATE NOT NULL,
    adresse TEXT,
    ville VARCHAR(100),
    pays VARCHAR(100) NOT NULL DEFAULT 'France',
    niveau_etudes VARCHAR(100),
    dernier_diplome VARCHAR(150),
    annee_obtention_diplome INTEGER CHECK (annee_obtention_diplome >= 1950 AND annee_obtention_diplome <= EXTRACT(YEAR FROM CURRENT_DATE)), -- R: Réalisme
    annees_experience INTEGER NOT NULL DEFAULT 0 CHECK (annees_experience >= 0), -- V: Valeurs, M: Mesure
    competences TEXT,
    cv_fichier VARCHAR(255), -- E: Existence du fichier
    lettre_motivation_fichier VARCHAR(255),
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut_global VARCHAR(50) NOT NULL DEFAULT 'Nouveau' CHECK (statut_global IN ('Nouveau', 'En évaluation', 'Accepté', 'Refusé', 'Embauché')),
    CONSTRAINT chk_age_minimum CHECK (EXTRACT(YEAR FROM AGE(CURRENT_DATE, date_naissance)) >= 16), -- R: Réalisme
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$') -- I: Irrégularité
);

-- Table Candidature
CREATE TABLE candidature (
    id_candidature SERIAL PRIMARY KEY,
    id_candidat INTEGER NOT NULL REFERENCES candidat(id_candidat),
    id_annonce INTEGER NOT NULL REFERENCES annonce_emploi(id_annonce),
    date_candidature TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- M: Mesure
    statut VARCHAR(50) NOT NULL DEFAULT 'Reçue' CHECK (statut IN ('Reçue', 'En cours d''examen', 'Présélectionnée', 'Acceptée', 'Refusée', 'En attente')),
    score_initial DECIMAL(5, 2) CHECK (score_initial >= 0 AND score_initial <= 100), -- V: Valeurs
    commentaire_initial TEXT,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_candidat, id_annonce), -- I: Irrégularité - éviter doublons
    CONSTRAINT chk_candidature_date CHECK (date_candidature <= CURRENT_TIMESTAMP) -- I: Irrégularité
);

-- Table Test et Évaluation
CREATE TABLE test_evaluation (
    id_test SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature),
    type_test VARCHAR(100) NOT NULL CHECK (type_test IN ('Technique', 'Psychotechnique', 'Langue', 'Personnalité', 'Cas pratique', 'Autre')),
    nom_test VARCHAR(150) NOT NULL,
    description TEXT,
    date_test DATE NOT NULL,
    heure_debut TIME,
    heure_fin TIME,
    duree_minutes INTEGER CHECK (duree_minutes > 0 AND duree_minutes <= 480), -- R: Réalisme - max 8h
    score_obtenu DECIMAL(5, 2) CHECK (score_obtenu >= 0 AND score_obtenu <= 100), -- V: Valeurs
    score_requis DECIMAL(5, 2) CHECK (score_requis >= 0 AND score_requis <= 100), -- V: Valeurs
    reussi BOOLEAN,
    commentaire TEXT,
    evaluateur VARCHAR(150),
    CONSTRAINT chk_test_date CHECK (date_test >= CURRENT_DATE - INTERVAL '1 year'), -- R: Réalisme
    CONSTRAINT chk_test_heures CHECK (heure_fin IS NULL OR heure_debut IS NULL OR heure_fin > heure_debut) -- I: Irrégularité
);

-- Table Entretien
CREATE TABLE entretien (
    id_entretien SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature),
    type_entretien VARCHAR(100) NOT NULL CHECK (type_entretien IN ('Téléphonique', 'Visio', 'Présentiel', 'Panel', 'Technique', 'RH')),
    date_entretien DATE NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin TIME,
    duree_minutes INTEGER CHECK (duree_minutes > 0 AND duree_minutes <= 240), -- R: Réalisme - max 4h
    lieu VARCHAR(200),
    intervieweurs TEXT NOT NULL, -- Liste des intervieweurs
    statut VARCHAR(50) NOT NULL DEFAULT 'Planifié' CHECK (statut IN ('Planifié', 'Confirmé', 'En cours', 'Terminé', 'Annulé', 'Reporté')),
    compte_rendu TEXT,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_entretien_date CHECK (date_entretien >= CURRENT_DATE - INTERVAL '6 months'), -- R: Réalisme
    CONSTRAINT chk_entretien_heures CHECK (heure_fin IS NULL OR heure_fin > heure_debut) -- I: Irrégularité
);

-- Table Résultat d'évaluation (Scoring)
CREATE TABLE resultat_evaluation (
    id_resultat SERIAL PRIMARY KEY,
    id_candidature INTEGER NOT NULL REFERENCES candidature(id_candidature),
    id_entretien INTEGER REFERENCES entretien(id_entretien),
    evaluateur VARCHAR(150) NOT NULL,
    date_evaluation DATE NOT NULL DEFAULT CURRENT_DATE, -- M: Mesure
    
    -- Critères d'évaluation détaillés (V: Valeurs)
    score_competences_techniques DECIMAL(5, 2) CHECK (score_competences_techniques >= 0 AND score_competences_techniques <= 100),
    score_experience DECIMAL(5, 2) CHECK (score_experience >= 0 AND score_experience <= 100),
    score_formation DECIMAL(5, 2) CHECK (score_formation >= 0 AND score_formation <= 100),
    score_soft_skills DECIMAL(5, 2) CHECK (score_soft_skills >= 0 AND score_soft_skills <= 100),
    score_motivation DECIMAL(5, 2) CHECK (score_motivation >= 0 AND score_motivation <= 100),
    score_adequation_poste DECIMAL(5, 2) CHECK (score_adequation_poste >= 0 AND score_adequation_poste <= 100),
    
    score_total DECIMAL(5, 2) CHECK (score_total >= 0 AND score_total <= 100), -- V: Valeurs
    decision VARCHAR(50) CHECK (decision IN ('Recommandé', 'À revoir', 'Non recommandé', 'Excellent', 'Bon', 'Moyen', 'Insuffisant')),
    commentaire TEXT,
    points_forts TEXT,
    points_amelioration TEXT
);

-- ==========================================
-- TABLES GESTION DES EMPLOYÉS
-- ==========================================

-- Table Employé
CREATE TABLE employe (
    id_employe SERIAL PRIMARY KEY,
    id_candidat INTEGER REFERENCES candidat(id_candidat), -- Lien avec le candidat d'origine
    matricule VARCHAR(50) NOT NULL UNIQUE, -- E: Existence
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email_professionnel VARCHAR(150) NOT NULL UNIQUE,
    telephone_professionnel VARCHAR(20),
    date_naissance DATE NOT NULL,
    sexe CHAR(1) CHECK (sexe IN ('M', 'F', 'A')), -- A: Autre
    adresse TEXT,
    ville VARCHAR(100),
    pays VARCHAR(100) NOT NULL DEFAULT 'France',
    
    -- Informations professionnelles
    id_poste INTEGER NOT NULL REFERENCES poste(id_poste),
    id_departement INTEGER NOT NULL REFERENCES departement(id_departement),
    date_embauche DATE NOT NULL, -- M: Mesure
    type_contrat VARCHAR(50) NOT NULL CHECK (type_contrat IN ('CDI', 'CDD', 'Stage', 'Alternance', 'Interim')),
    statut_employe VARCHAR(50) NOT NULL DEFAULT 'Actif' CHECK (statut_employe IN ('Actif', 'En congé', 'Suspendu', 'Démissionné', 'Licencié', 'Retraité')), -- A: Appartenance
    
    -- Informations salariales (V: Valeurs, R: Réalisme)
    salaire_base DECIMAL(10, 2) NOT NULL CHECK (salaire_base > 0),
    devise VARCHAR(3) NOT NULL DEFAULT 'EUR',
    
    -- Informations administratives
    numero_securite_sociale VARCHAR(50) UNIQUE, -- E: Existence
    numero_compte_bancaire VARCHAR(50),
    date_fin_contrat DATE, -- Pour CDD
    
    -- Métadonnées
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_derniere_modification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_employe_age CHECK (EXTRACT(YEAR FROM AGE(CURRENT_DATE, date_naissance)) >= 16), -- R: Réalisme
    CONSTRAINT chk_date_embauche CHECK (date_embauche <= CURRENT_DATE), -- I: Irrégularité
    CONSTRAINT chk_date_fin_contrat CHECK (date_fin_contrat IS NULL OR date_fin_contrat > date_embauche), -- I: Irrégularité
    CONSTRAINT chk_salaire_realiste CHECK (salaire_base <= 1000000) -- R: Réalisme
);

-- Table Contrat de travail
CREATE TABLE contrat_travail (
    id_contrat SERIAL PRIMARY KEY,
    id_employe INTEGER NOT NULL REFERENCES employe(id_employe),
    type_contrat VARCHAR(50) NOT NULL CHECK (type_contrat IN ('Essai', 'CDI', 'CDD', 'Stage', 'Alternance')),
    numero_contrat VARCHAR(100) NOT NULL UNIQUE, -- E: Existence
    date_debut DATE NOT NULL,
    date_fin DATE,
    duree_essai_mois INTEGER CHECK (duree_essai_mois >= 0 AND duree_essai_mois <= 6), -- R: Réalisme - max 6 mois
    essai_renouvelable BOOLEAN DEFAULT FALSE,
    essai_renouvele BOOLEAN DEFAULT FALSE,
    date_renouvellement_essai DATE,
    
    -- Conditions contractuelles
    salaire_brut DECIMAL(10, 2) NOT NULL CHECK (salaire_brut > 0), -- V: Valeurs
    primes_annuelles DECIMAL(10, 2) DEFAULT 0 CHECK (primes_annuelles >= 0),
    avantages TEXT,
    heures_hebdomadaires DECIMAL(5, 2) NOT NULL CHECK (heures_hebdomadaires > 0 AND heures_hebdomadaires <= 48), -- R: Réalisme
    jours_conges_annuels INTEGER NOT NULL DEFAULT 25 CHECK (jours_conges_annuels >= 0 AND jours_conges_annuels <= 60), -- R: Réalisme
    
    -- Statut
    statut VARCHAR(50) NOT NULL DEFAULT 'Actif' CHECK (statut IN ('Actif', 'Terminé', 'Suspendu', 'Annulé')),
    motif_fin_contrat TEXT,
    date_signature DATE,
    fichier_contrat VARCHAR(255), -- E: Existence du fichier
    
    CONSTRAINT chk_contrat_dates CHECK (date_fin IS NULL OR date_fin > date_debut), -- I: Irrégularité
    CONSTRAINT chk_contrat_essai CHECK (
        (type_contrat != 'Essai') OR 
        (type_contrat = 'Essai' AND duree_essai_mois IS NOT NULL AND duree_essai_mois <= 6)
    ), -- R: Réalisme - max 6 mois d'essai
    CONSTRAINT chk_renouvellement_essai CHECK (
        (NOT essai_renouvele) OR 
        (essai_renouvele AND essai_renouvelable AND date_renouvellement_essai IS NOT NULL)
    ) -- I: Irrégularité
);

-- Table Affiliation sociale et administrative
CREATE TABLE affiliation_sociale (
    id_affiliation SERIAL PRIMARY KEY,
    id_employe INTEGER NOT NULL REFERENCES employe(id_employe),
    type_affiliation VARCHAR(100) NOT NULL CHECK (type_affiliation IN ('Sécurité Sociale', 'Mutuelle', 'Retraite', 'Prévoyance', 'Chômage', 'Autre')),
    organisme VARCHAR(150) NOT NULL,
    numero_affiliation VARCHAR(100) NOT NULL, -- E: Existence
    date_affiliation DATE NOT NULL DEFAULT CURRENT_DATE, -- M: Mesure
    date_fin_affiliation DATE,
    statut VARCHAR(50) NOT NULL DEFAULT 'Active' CHECK (statut IN ('Active', 'Suspendue', 'Terminée')), -- A: Appartenance
    cotisation_mensuelle DECIMAL(10, 2) CHECK (cotisation_mensuelle >= 0), -- V: Valeurs
    commentaire TEXT,
    
    UNIQUE(id_employe, type_affiliation, organisme), -- I: Irrégularité - éviter doublons
    CONSTRAINT chk_affiliation_dates CHECK (date_fin_affiliation IS NULL OR date_fin_affiliation >= date_affiliation) -- I: Irrégularité
);

-- ==========================================
-- INDEX POUR OPTIMISATION DES PERFORMANCES
-- ==========================================

-- Index pour recherches fréquentes
CREATE INDEX idx_candidat_email ON candidat(email);
CREATE INDEX idx_candidat_statut ON candidat(statut_global);
CREATE INDEX idx_candidature_statut ON candidature(statut);
CREATE INDEX idx_candidature_date ON candidature(date_candidature);
CREATE INDEX idx_annonce_statut ON annonce_emploi(statut);
CREATE INDEX idx_annonce_date_pub ON annonce_emploi(date_publication);
CREATE INDEX idx_employe_matricule ON employe(matricule);
CREATE INDEX idx_employe_statut ON employe(statut_employe);
CREATE INDEX idx_employe_departement ON employe(id_departement);
CREATE INDEX idx_contrat_type ON contrat_travail(type_contrat);
CREATE INDEX idx_contrat_statut ON contrat_travail(statut);

-- ==========================================
-- VUES UTILES POUR REPORTING
-- ==========================================

-- Vue: Statistiques de recrutement par poste
CREATE OR REPLACE VIEW v_statistiques_recrutement AS
SELECT 
    p.titre_poste,
    d.nom_departement,
    COUNT(DISTINCT c.id_candidature) AS nombre_candidatures,
    COUNT(DISTINCT CASE WHEN c.statut = 'Présélectionnée' THEN c.id_candidature END) AS preselectionnes,
    COUNT(DISTINCT CASE WHEN c.statut = 'Acceptée' THEN c.id_candidature END) AS acceptes,
    AVG(c.score_initial) AS score_moyen
FROM annonce_emploi a
JOIN poste p ON a.id_poste = p.id_poste
JOIN departement d ON p.id_departement = d.id_departement
LEFT JOIN candidature c ON a.id_annonce = c.id_annonce
GROUP BY p.titre_poste, d.nom_departement;

-- Vue: Employés actifs avec informations complètes
CREATE OR REPLACE VIEW v_employes_actifs AS
SELECT 
    e.id_employe,
    e.matricule,
    e.nom,
    e.prenom,
    e.email_professionnel,
    p.titre_poste,
    d.nom_departement,
    e.date_embauche,
    EXTRACT(YEAR FROM AGE(CURRENT_DATE, e.date_embauche))::INTEGER AS anciennete_annees,
    e.type_contrat,
    e.salaire_base,
    e.statut_employe
FROM employe e
JOIN poste p ON e.id_poste = p.id_poste
JOIN departement d ON e.id_departement = d.id_departement
WHERE e.statut_employe = 'Actif';

-- Vue: Contrats d'essai en cours
CREATE OR REPLACE VIEW v_contrats_essai AS
SELECT 
    e.matricule,
    e.nom,
    e.prenom,
    p.titre_poste,
    ct.date_debut,
    ct.date_debut + (ct.duree_essai_mois || ' months')::INTERVAL AS date_fin_essai,
    ct.duree_essai_mois,
    ct.essai_renouvelable,
    ct.essai_renouvele
FROM contrat_travail ct
JOIN employe e ON ct.id_employe = e.id_employe
JOIN poste p ON e.id_poste = p.id_poste
WHERE ct.type_contrat = 'Essai' AND ct.statut = 'Actif';

-- ==========================================
-- COMMENTAIRES SUR LES TABLES (DOCUMENTATION)
-- ==========================================

COMMENT ON TABLE departement IS 'Départements de l''entreprise';
COMMENT ON TABLE poste IS 'Postes disponibles dans l''entreprise';
COMMENT ON TABLE annonce_emploi IS 'Annonces de recrutement publiées';
COMMENT ON TABLE candidat IS 'Candidats ayant postulé';
COMMENT ON TABLE candidature IS 'Candidatures soumises par les candidats';
COMMENT ON TABLE test_evaluation IS 'Tests et évaluations des candidats';
COMMENT ON TABLE entretien IS 'Entretiens programmés et réalisés';
COMMENT ON TABLE resultat_evaluation IS 'Résultats et scoring des évaluations';
COMMENT ON TABLE employe IS 'Employés de l''entreprise';
COMMENT ON TABLE contrat_travail IS 'Contrats de travail incluant périodes d''essai';
COMMENT ON TABLE affiliation_sociale IS 'Affiliations sociales et administratives des employés';

-- ==========================================
-- DONNÉES DE DÉMONSTRATION (OPTIONNEL)
-- ==========================================

-- Insertion de départements
INSERT INTO departement (nom_departement, description, budget_annuel) VALUES
('Ressources Humaines', 'Gestion du personnel et recrutement', 500000.00),
('Informatique', 'Développement et infrastructure IT', 1200000.00),
('Marketing', 'Communication et promotion', 800000.00),
('Finance', 'Comptabilité et gestion financière', 600000.00),
('Production', 'Fabrication et production', 1500000.00);

-- Insertion de postes
INSERT INTO poste (titre_poste, id_departement, description_poste, salaire_min, salaire_max, niveau_experience_requis, diplome_requis) VALUES
('Développeur Full Stack', 2, 'Développement d''applications web', 35000.00, 55000.00, 'Intermédiaire', 'Licence en Informatique'),
('Chargé de Recrutement', 1, 'Gestion du processus de recrutement', 28000.00, 40000.00, 'Junior', 'Master en RH'),
('Responsable Marketing', 3, 'Stratégie marketing et communication', 45000.00, 65000.00, 'Senior', 'Master en Marketing'),
('Comptable', 4, 'Gestion comptable et fiscale', 30000.00, 45000.00, 'Intermédiaire', 'Licence en Comptabilité'),
('Chef de Production', 5, 'Supervision de la production', 40000.00, 60000.00, 'Senior', 'Diplôme d''Ingénieur');

-- ==========================================
-- FIN DU SCRIPT
-- ==========================================

-- Script créé pour la gestion des ressources humaines
-- Respectant les critères VAMPIRE :
-- V - Valeurs : Salaires, scores, budgets avec contraintes numériques
-- A - Appartenance : Statuts pour déterminer l'appartenance (Actif, Membre, etc.)
-- M - Mesure : Dates et durées pour mesurer le temps et l'évolution
-- P - Présentation : Structure claire avec vues et index
-- I - Irrégularité : Contraintes CHECK pour éviter les incohérences
-- R - Réalisme : Limites réalistes sur salaires, durées, âges
-- E - Existence : Clés uniques et contraintes NOT NULL pour garantir l'existence
