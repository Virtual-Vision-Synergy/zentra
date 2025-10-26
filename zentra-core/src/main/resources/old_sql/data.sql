-- =====================================================
-- Script d'insertion des données de test
-- =====================================================

-- =====================================================
-- 1. INSERTION DES CANDIDATS
-- =====================================================

INSERT INTO candidate (created_at, updated_at, last_name, first_name, email, phone, birth_date, address, city, country, education_level, last_degree, years_experience, skills, cv_file, motivational_letter_file)
VALUES
    (NOW(), NOW(), 'Dupont', 'Jean', 'jean.dupont@email.com', '0612345678', '1995-03-15', '15 Rue de la Paix', 'Paris', 'France', 'Master', 'Master Informatique', 3, 'Java, Spring Boot, React, SQL', 'cv_dupont.pdf', 'lettre_dupont.pdf'),
    (NOW(), NOW(), 'Martin', 'Sophie', 'sophie.martin@email.com', '0623456789', '1998-07-22', '28 Avenue des Champs', 'Lyon', 'France', 'Licence', 'Licence Développement Web', 2, 'JavaScript, Vue.js, Node.js, MongoDB', 'cv_martin.pdf', 'lettre_martin.pdf'),
    (NOW(), NOW(), 'Dubois', 'Pierre', 'pierre.dubois@email.com', '0634567890', '1993-11-08', '42 Boulevard Voltaire', 'Marseille', 'France', 'Master', 'Master Data Science', 5, 'Python, Machine Learning, SQL, Docker', 'cv_dubois.pdf', 'lettre_dubois.pdf'),
    (NOW(), NOW(), 'Bernard', 'Marie', 'marie.bernard@email.com', '0645678901', '1996-05-30', '7 Rue Victor Hugo', 'Toulouse', 'France', 'Ingénieur', 'Diplôme Ingénieur Informatique', 4, 'Java, Angular, Microservices, Kubernetes', 'cv_bernard.pdf', 'lettre_bernard.pdf'),
    (NOW(), NOW(), 'Petit', 'Thomas', 'thomas.petit@email.com', '0656789012', '1999-01-18', '33 Rue de la République', 'Nantes', 'France', 'Licence', 'Licence Informatique', 1, 'PHP, Laravel, MySQL, Git', 'cv_petit.pdf', 'lettre_petit.pdf');

-- =====================================================
-- 2. INSERTION DES QCM
-- =====================================================

INSERT INTO qcm (created_at, updated_at, title, description, duration_minutes, total_score, required_score)
VALUES
    (NOW(), NOW(), 'QCM Java Developer', 'Évaluation des compétences en développement Java', 45, 100.0, 60.0),
    (NOW(), NOW(), 'QCM JavaScript Developer', 'Évaluation des compétences en JavaScript et frameworks modernes', 30, 100.0, 65.0),
    (NOW(), NOW(), 'QCM Data Scientist', 'Évaluation des compétences en Data Science et Machine Learning', 60, 100.0, 70.0);

-- =====================================================
-- 3. INSERTION DES QUESTIONS
-- =====================================================

-- Questions pour QCM Java Developer (QCM 1)
INSERT INTO question (created_at, updated_at, libelle, required, score, qcm_id)
VALUES
    (NOW(), NOW(), 'Quelle est la différence entre == et equals() en Java?', true, 20.0, 1),
    (NOW(), NOW(), 'Qu''est-ce qu''une interface fonctionnelle en Java 8?', true, 20.0, 1),
    (NOW(), NOW(), 'Quel est le rôle de l''annotation @Transactional dans Spring?', true, 20.0, 1),
    (NOW(), NOW(), 'Qu''est-ce que le principe SOLID en programmation orientée objet?', true, 20.0, 1),
    (NOW(), NOW(), 'Comment gérer les exceptions en Java?', true, 20.0, 1);

-- Questions pour QCM JavaScript Developer (QCM 2)
INSERT INTO question (created_at, updated_at, libelle, required, score, qcm_id)
VALUES
    (NOW(), NOW(), 'Quelle est la différence entre let, const et var en JavaScript?', true, 25.0, 2),
    (NOW(), NOW(), 'Qu''est-ce qu''une Promise en JavaScript?', true, 25.0, 2),
    (NOW(), NOW(), 'Comment fonctionne le Virtual DOM dans React?', true, 25.0, 2),
    (NOW(), NOW(), 'Qu''est-ce que l''event loop en JavaScript?', true, 25.0, 2);

-- Questions pour QCM Data Scientist (QCM 3)
INSERT INTO question (created_at, updated_at, libelle, required, score, qcm_id)
VALUES
    (NOW(), NOW(), 'Quelle est la différence entre régression et classification?', true, 20.0, 3),
    (NOW(), NOW(), 'Qu''est-ce que le surapprentissage (overfitting)?', true, 20.0, 3),
    (NOW(), NOW(), 'Expliquez l''algorithme K-means', true, 20.0, 3),
    (NOW(), NOW(), 'Qu''est-ce qu''un neurone artificiel?', true, 20.0, 3),
    (NOW(), NOW(), 'Comment évaluer la performance d''un modèle de classification?', true, 20.0, 3);

-- =====================================================
-- 4. INSERTION DES CHOIX DE RÉPONSE
-- =====================================================

-- Choix pour Question 1 (== vs equals)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), '== compare les références, equals() compare le contenu', true, 1),
    (NOW(), NOW(), '== compare le contenu, equals() compare les références', false, 1),
    (NOW(), NOW(), 'Aucune différence', false, 1),
    (NOW(), NOW(), '== est plus rapide que equals()', false, 1);

-- Choix pour Question 2 (Interface fonctionnelle)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Une interface avec une seule méthode abstraite', true, 2),
    (NOW(), NOW(), 'Une interface qui peut contenir plusieurs méthodes', false, 2),
    (NOW(), NOW(), 'Une classe abstraite avec des fonctions', false, 2),
    (NOW(), NOW(), 'Une interface qui ne peut pas être implémentée', false, 2);

-- Choix pour Question 3 (@Transactional)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Gère automatiquement les transactions de base de données', true, 3),
    (NOW(), NOW(), 'Permet de faire des requêtes SQL', false, 3),
    (NOW(), NOW(), 'Définit une classe comme repository', false, 3),
    (NOW(), NOW(), 'Crée automatiquement les tables', false, 3);

-- Choix pour Question 4 (SOLID)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Ensemble de 5 principes de conception orientée objet', true, 4),
    (NOW(), NOW(), 'Un design pattern', false, 4),
    (NOW(), NOW(), 'Un framework Java', false, 4),
    (NOW(), NOW(), 'Une bibliothèque de tests', false, 4);

-- Choix pour Question 5 (Exceptions)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Avec les blocs try-catch-finally', true, 5),
    (NOW(), NOW(), 'Avec l''annotation @Exception', false, 5),
    (NOW(), NOW(), 'Avec la méthode handleException()', false, 5),
    (NOW(), NOW(), 'Les exceptions ne peuvent pas être gérées', false, 5);

-- Choix pour Question 6 (let, const, var)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'let a une portée de bloc, const est constante, var a une portée de fonction', true, 6),
    (NOW(), NOW(), 'Aucune différence entre les trois', false, 6),
    (NOW(), NOW(), 'let est globale, const et var sont locales', false, 6),
    (NOW(), NOW(), 'var est obsolète et ne doit jamais être utilisé', false, 6);

-- Choix pour Question 7 (Promise)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Un objet représentant une opération asynchrone', true, 7),
    (NOW(), NOW(), 'Une fonction synchrone', false, 7),
    (NOW(), NOW(), 'Un type de variable', false, 7),
    (NOW(), NOW(), 'Une bibliothèque JavaScript', false, 7);

-- Choix pour Question 8 (Virtual DOM)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Une représentation en mémoire du DOM réel pour optimiser les mises à jour', true, 8),
    (NOW(), NOW(), 'Une copie exacte du DOM', false, 8),
    (NOW(), NOW(), 'Un DOM caché dans le navigateur', false, 8),
    (NOW(), NOW(), 'Un DOM virtuel dans le cloud', false, 8);

-- Choix pour Question 9 (Event loop)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Mécanisme qui gère l''exécution du code asynchrone', true, 9),
    (NOW(), NOW(), 'Une boucle for améliorée', false, 9),
    (NOW(), NOW(), 'Un gestionnaire d''événements DOM', false, 9),
    (NOW(), NOW(), 'Une fonction récursive', false, 9);

-- Choix pour Question 10 (Régression vs Classification)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Régression prédit des valeurs continues, classification prédit des catégories', true, 10),
    (NOW(), NOW(), 'Régression et classification sont identiques', false, 10),
    (NOW(), NOW(), 'Classification prédit des nombres, régression prédit des catégories', false, 10),
    (NOW(), NOW(), 'Régression est supervisé, classification est non-supervisé', false, 10);

-- Choix pour Question 11 (Overfitting)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Le modèle apprend trop bien les données d''entraînement et performe mal sur de nouvelles données', true, 11),
    (NOW(), NOW(), 'Le modèle ne converge pas', false, 11),
    (NOW(), NOW(), 'Le modèle est trop simple', false, 11),
    (NOW(), NOW(), 'Le modèle manque de données', false, 11);

-- Choix pour Question 12 (K-means)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Algorithme de clustering qui partitionne les données en K groupes', true, 12),
    (NOW(), NOW(), 'Algorithme de régression linéaire', false, 12),
    (NOW(), NOW(), 'Algorithme de classification supervisée', false, 12),
    (NOW(), NOW(), 'Algorithme de réduction de dimensionnalité', false, 12);

-- Choix pour Question 13 (Neurone artificiel)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Unité de calcul qui effectue une somme pondérée suivie d''une fonction d''activation', true, 13),
    (NOW(), NOW(), 'Une cellule biologique', false, 13),
    (NOW(), NOW(), 'Un type de base de données', false, 13),
    (NOW(), NOW(), 'Un algorithme de tri', false, 13);

-- Choix pour Question 14 (Évaluation classification)
INSERT INTO choice (created_at, updated_at, libelle, correct, question_id)
VALUES
    (NOW(), NOW(), 'Avec des métriques comme précision, rappel, F1-score, matrice de confusion', true, 14),
    (NOW(), NOW(), 'Uniquement avec l''erreur quadratique moyenne', false, 14),
    (NOW(), NOW(), 'En comptant le nombre de lignes de code', false, 14),
    (NOW(), NOW(), 'Avec le temps d''exécution', false, 14);

-- =====================================================
-- 5. INSERTION DES APPLICATIONS
-- =====================================================

INSERT INTO application (created_at, updated_at, applied_at, status, document_score, score, comment, candidate_id, qcm_id, publication_id)
VALUES
    (NOW(), NOW(), NOW(), 'Received', NULL, NULL, NULL, 1, 1, NULL),
    (NOW(), NOW(), NOW(), 'Received', NULL, NULL, NULL, 2, 2, NULL),
    (NOW(), NOW(), NOW(), 'Received', NULL, NULL, NULL, 3, 3, NULL),
    (NOW(), NOW(), NOW(), 'Received', NULL, NULL, NULL, 4, 1, NULL),
    (NOW(), NOW(), NOW(), 'Received', NULL, NULL, NULL, 5, 2, NULL);

-- =====================================================
-- 6. INSERTION DES TOKENS
-- =====================================================

INSERT INTO token (created_at, updated_at, value, active, expiration_date, application_id)
VALUES
    (NOW(), NOW(), 'TOKEN_DUPONT_2025', true, now() + interval '7 days', 1),
    (NOW(), NOW(), 'TOKEN_MARTIN_2025', true, now() + interval '7 days', 2),
    (NOW(), NOW(), 'TOKEN_DUBOIS_2025', true, now() + interval '7 days', 3),
    (NOW(), NOW(), 'TOKEN_BERNARD_2025', true, now() + interval '7 days', 4),
    (NOW(), NOW(), 'TOKEN_PETIT_2025', true, now() + interval '7 days', 5);

-- =====================================================
-- FIN DU SCRIPT
-- =====================================================

