-- ==========================================
-- TEST - Exemple de flux complet de recrutement
-- Démonstration du processus de recrutement complet
-- De l'annonce d'emploi jusqu'à l'embauche
-- ==========================================

-- Ce fichier peut être exécuté après schema_hr.sql pour tester le système

BEGIN;

-- ==========================================
-- ÉTAPE 1: Définition du besoin et publication d'annonce
-- ==========================================

-- Créer une nouvelle annonce d'emploi pour un développeur
INSERT INTO annonce_emploi (
    id_poste, 
    titre_annonce, 
    description, 
    type_contrat, 
    lieu_travail,
    nombre_postes,
    canaux_publication
) VALUES (
    1, -- Développeur Full Stack
    'Recherche Développeur Full Stack Junior H/F',
    'Nous recherchons un développeur passionné pour rejoindre notre équipe. Vous travaillerez sur des projets innovants utilisant les dernières technologies.',
    'CDI',
    'Paris, France',
    2,
    'LinkedIn, Indeed, Site carrière entreprise'
);

-- Récupérer l'ID de l'annonce créée
SELECT id_annonce, titre_annonce, statut, date_publication 
FROM annonce_emploi 
WHERE titre_annonce = 'Recherche Développeur Full Stack Junior H/F';

-- ==========================================
-- ÉTAPE 2 & 3: Réception et enregistrement des candidatures
-- ==========================================

-- Candidat 1: Jean Dupont
INSERT INTO candidat (
    nom, prenom, email, telephone, date_naissance,
    adresse, ville, pays,
    niveau_etudes, dernier_diplome, annee_obtention_diplome,
    annees_experience, competences,
    cv_fichier, lettre_motivation_fichier
) VALUES (
    'Dupont', 'Jean', 'jean.dupont@email.com', '0601020304',
    '1995-05-15',
    '123 Rue de la Paix', 'Paris', 'France',
    'Master', 'Master Informatique', 2018,
    3, 'JavaScript, React, Node.js, PostgreSQL, Git',
    'cv_jean_dupont.pdf', 'lm_jean_dupont.pdf'
);

-- Candidat 2: Marie Martin
INSERT INTO candidat (
    nom, prenom, email, telephone, date_naissance,
    ville, pays,
    niveau_etudes, dernier_diplome, annee_obtention_diplome,
    annees_experience, competences,
    cv_fichier, lettre_motivation_fichier
) VALUES (
    'Martin', 'Marie', 'marie.martin@email.com', '0612345678',
    '1998-08-20',
    'Lyon', 'France',
    'Licence', 'Licence Informatique', 2020,
    2, 'Python, Django, React, MySQL, Docker',
    'cv_marie_martin.pdf', 'lm_marie_martin.pdf'
);

-- Candidat 3: Ahmed Ben Ali
INSERT INTO candidat (
    nom, prenom, email, telephone, date_naissance,
    ville, pays,
    niveau_etudes, dernier_diplome, annee_obtention_diplome,
    annees_experience, competences,
    cv_fichier
) VALUES (
    'Ben Ali', 'Ahmed', 'ahmed.benali@email.com', '0623456789',
    '1992-03-10',
    'Marseille', 'France',
    'Master', 'Master Génie Logiciel', 2016,
    5, 'Java, Spring Boot, Angular, MongoDB, Kubernetes',
    'cv_ahmed_benali.pdf'
);

-- ==========================================
-- ÉTAPE 4: Traitement et classification des dossiers (Candidatures)
-- ==========================================

-- Créer les candidatures avec scores initiaux
INSERT INTO candidature (id_candidat, id_annonce, score_initial, statut, commentaire_initial) VALUES
(1, 1, 75.00, 'Présélectionnée', 'Bon profil, expérience pertinente en Full Stack'),
(2, 1, 68.00, 'Présélectionnée', 'Profil prometteur, moins d''expérience mais compétences solides'),
(3, 1, 85.00, 'Présélectionnée', 'Excellent profil, expérience significative');

-- ==========================================
-- ÉTAPE 5: Organisation de tests et évaluations
-- ==========================================

-- Tests techniques pour les candidats présélectionnés
INSERT INTO test_evaluation (
    id_candidature, type_test, nom_test, description,
    date_test, heure_debut, heure_fin, duree_minutes,
    score_obtenu, score_requis, reussi, evaluateur
) VALUES
-- Tests pour Jean Dupont
(1, 'Technique', 'Test JavaScript/React', 'Évaluation des compétences en développement front-end',
 CURRENT_DATE + INTERVAL '3 days', '10:00', '12:00', 120,
 78.00, 70.00, TRUE, 'Tech Lead - Pierre Dubois'),
 
-- Tests pour Marie Martin
(2, 'Technique', 'Test JavaScript/React', 'Évaluation des compétences en développement front-end',
 CURRENT_DATE + INTERVAL '3 days', '14:00', '16:00', 120,
 72.00, 70.00, TRUE, 'Tech Lead - Pierre Dubois'),
 
-- Tests pour Ahmed Ben Ali
(3, 'Technique', 'Test JavaScript/React', 'Évaluation des compétences en développement front-end',
 CURRENT_DATE + INTERVAL '3 days', '09:00', '11:00', 120,
 88.00, 70.00, TRUE, 'Tech Lead - Pierre Dubois');

-- ==========================================
-- ÉTAPE 6: Planification des entretiens
-- ==========================================

-- Entretiens pour les candidats ayant réussi les tests
INSERT INTO entretien (
    id_candidature, type_entretien, date_entretien,
    heure_debut, heure_fin, duree_minutes, lieu,
    intervieweurs, statut
) VALUES
-- Entretien RH Jean Dupont
(1, 'RH', CURRENT_DATE + INTERVAL '7 days',
 '14:00', '15:00', 60, 'Salle de réunion A - Bureau Paris',
 'Sophie Lambert (RH), Pierre Dubois (Tech Lead)', 'Planifié'),

-- Entretien RH Marie Martin
(2, 'RH', CURRENT_DATE + INTERVAL '7 days',
 '15:30', '16:30', 60, 'Salle de réunion A - Bureau Paris',
 'Sophie Lambert (RH), Pierre Dubois (Tech Lead)', 'Planifié'),

-- Entretien RH Ahmed Ben Ali
(3, 'RH', CURRENT_DATE + INTERVAL '7 days',
 '10:00', '11:00', 60, 'Salle de réunion B - Bureau Paris',
 'Sophie Lambert (RH), Marc Durand (CTO)', 'Planifié');

-- ==========================================
-- ÉTAPE 7: Scoring / Évaluation des candidats
-- ==========================================

-- Évaluations détaillées après entretiens
INSERT INTO resultat_evaluation (
    id_candidature, id_entretien, evaluateur, date_evaluation,
    score_competences_techniques, score_experience, score_formation,
    score_soft_skills, score_motivation, score_adequation_poste,
    score_total, decision, commentaire, points_forts, points_amelioration
) VALUES
-- Évaluation Jean Dupont
(1, 1, 'Sophie Lambert', CURRENT_DATE + INTERVAL '7 days',
 80.00, 75.00, 85.00, 82.00, 88.00, 80.00,
 81.67, 'Recommandé',
 'Excellent candidat avec un bon équilibre entre compétences techniques et soft skills',
 'Très motivé, bonnes compétences en communication, expérience pertinente',
 'Pourrait développer ses compétences en leadership'),

-- Évaluation Marie Martin
(2, 2, 'Sophie Lambert', CURRENT_DATE + INTERVAL '7 days',
 75.00, 70.00, 78.00, 85.00, 90.00, 78.00,
 79.33, 'Recommandé',
 'Candidate prometteuse avec une forte motivation et un bon potentiel',
 'Très motivée, excellentes soft skills, bonne capacité d''apprentissage',
 'Manque un peu d''expérience mais compense par sa motivation'),

-- Évaluation Ahmed Ben Ali
(3, 3, 'Marc Durand', CURRENT_DATE + INTERVAL '7 days',
 92.00, 88.00, 90.00, 85.00, 80.00, 90.00,
 87.50, 'Excellent',
 'Candidat exceptionnel avec une expérience solide et des compétences très avancées',
 'Expertise technique remarquable, expérience significative, autonome',
 'Pourrait être surqualifié pour le poste, risque de partir rapidement');

-- ==========================================
-- ÉTAPE 8: Communication des résultats (simulation)
-- ==========================================

-- Mettre à jour le statut des candidatures
UPDATE candidature SET statut = 'Acceptée', date_derniere_modification = CURRENT_TIMESTAMP
WHERE id_candidature IN (1, 2); -- Jean et Marie sont acceptés

UPDATE candidature SET statut = 'En attente', date_derniere_modification = CURRENT_TIMESTAMP
WHERE id_candidature = 3; -- Ahmed en attente (surqualifié)

UPDATE candidat SET statut_global = 'Accepté'
WHERE id_candidat IN (1, 2);

UPDATE candidat SET statut_global = 'En évaluation'
WHERE id_candidat = 3;

-- Fermer l'annonce (postes pourvus)
UPDATE annonce_emploi SET statut = 'Pourvue'
WHERE id_annonce = 1;

-- ==========================================
-- ÉTAPE 9: Établissement du contrat d'essai
-- ==========================================

-- Créer les employés (transformation Candidat → Employé)
INSERT INTO employe (
    id_candidat, matricule, nom, prenom, email_professionnel, telephone_professionnel,
    date_naissance, sexe, adresse, ville, pays,
    id_poste, id_departement, date_embauche, type_contrat, salaire_base
) VALUES
-- Jean Dupont devient employé
(1, 'EMP2025001', 'Dupont', 'Jean', 'jean.dupont@entreprise.com', '0601020304',
 '1995-05-15', 'M', '123 Rue de la Paix', 'Paris', 'France',
 1, 2, CURRENT_DATE, 'CDI', 42000.00),

-- Marie Martin devient employée
(2, 'EMP2025002', 'Martin', 'Marie', 'marie.martin@entreprise.com', '0612345678',
 '1998-08-20', 'F', NULL, 'Lyon', 'France',
 1, 2, CURRENT_DATE, 'CDI', 38000.00);

-- Créer les contrats d'essai (maximum 6 mois, renouvelables une fois)
INSERT INTO contrat_travail (
    id_employe, type_contrat, numero_contrat, date_debut,
    duree_essai_mois, essai_renouvelable, essai_renouvele,
    salaire_brut, heures_hebdomadaires, jours_conges_annuels,
    statut, date_signature
) VALUES
-- Contrat Jean Dupont
(1, 'Essai', 'CONT2025001', CURRENT_DATE,
 3, TRUE, FALSE,
 42000.00, 35.00, 25,
 'Actif', CURRENT_DATE - INTERVAL '5 days'),

-- Contrat Marie Martin
(2, 'Essai', 'CONT2025002', CURRENT_DATE,
 4, TRUE, FALSE,
 38000.00, 35.00, 25,
 'Actif', CURRENT_DATE - INTERVAL '5 days');

-- Mettre à jour le statut des candidats
UPDATE candidat SET statut_global = 'Embauché'
WHERE id_candidat IN (1, 2);

-- ==========================================
-- ÉTAPE 10: Affiliation du personnel aux procédures sociales
-- ==========================================

-- Affiliations pour Jean Dupont
INSERT INTO affiliation_sociale (
    id_employe, type_affiliation, organisme, numero_affiliation,
    date_affiliation, statut, cotisation_mensuelle
) VALUES
(1, 'Sécurité Sociale', 'CPAM Paris', 'SS2025001',
 CURRENT_DATE, 'Active', 450.00),
(1, 'Mutuelle', 'Mutuelle Entreprise Pro', 'MUT2025001',
 CURRENT_DATE, 'Active', 75.00),
(1, 'Retraite', 'Caisse Nationale de Retraite', 'RET2025001',
 CURRENT_DATE, 'Active', 200.00);

-- Affiliations pour Marie Martin
INSERT INTO affiliation_sociale (
    id_employe, type_affiliation, organisme, numero_affiliation,
    date_affiliation, statut, cotisation_mensuelle
) VALUES
(2, 'Sécurité Sociale', 'CPAM Rhône', 'SS2025002',
 CURRENT_DATE, 'Active', 420.00),
(2, 'Mutuelle', 'Mutuelle Entreprise Pro', 'MUT2025002',
 CURRENT_DATE, 'Active', 75.00),
(2, 'Retraite', 'Caisse Nationale de Retraite', 'RET2025002',
 CURRENT_DATE, 'Active', 180.00);

-- ==========================================
-- VÉRIFICATION ET REPORTING
-- ==========================================

-- Statistiques de recrutement
SELECT * FROM v_statistiques_recrutement;

-- Liste des employés actifs
SELECT * FROM v_employes_actifs;

-- Contrats d'essai en cours
SELECT * FROM v_contrats_essai;

-- Résumé du processus de recrutement
SELECT 
    c.nom || ' ' || c.prenom AS candidat,
    ca.statut AS statut_candidature,
    te.score_obtenu AS score_test,
    re.score_total AS score_evaluation,
    re.decision,
    CASE WHEN e.id_employe IS NOT NULL THEN 'Embauché' ELSE 'Non embauché' END AS resultat_final
FROM candidat c
JOIN candidature ca ON c.id_candidat = ca.id_candidat
LEFT JOIN test_evaluation te ON ca.id_candidature = te.id_candidature
LEFT JOIN resultat_evaluation re ON ca.id_candidature = re.id_candidature
LEFT JOIN employe e ON c.id_candidat = e.id_candidat
WHERE ca.id_annonce = 1
ORDER BY re.score_total DESC NULLS LAST;

COMMIT;

-- ==========================================
-- FIN DU TEST
-- ==========================================
-- Le flux complet de recrutement a été testé avec succès
-- 3 candidats ont postulé
-- 3 ont été présélectionnés et testés
-- 2 ont été embauchés avec contrats d'essai
-- Toutes les affiliations sociales ont été créées
