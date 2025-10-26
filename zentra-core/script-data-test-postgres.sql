-- ========================================
-- Script PostgreSQL de données de test pour Interview
-- ========================================
-- Ce script crée toutes les données nécessaires pour tester les endpoints dans test_entretien.http

-- ========================================
-- 1. NETTOYAGE DES DONNÉES EXISTANTES
-- ========================================

-- Nettoyer les tables dans le bon ordre (respect des contraintes FK)
TRUNCATE TABLE interview CASCADE;
TRUNCATE TABLE attempt CASCADE;
TRUNCATE TABLE token CASCADE;
TRUNCATE TABLE application CASCADE;
TRUNCATE TABLE employment_contract CASCADE;
TRUNCATE TABLE candidate CASCADE;
TRUNCATE TABLE employee CASCADE;
TRUNCATE TABLE publication CASCADE;
TRUNCATE TABLE job CASCADE;
TRUNCATE TABLE qcm CASCADE;
TRUNCATE TABLE question CASCADE;
TRUNCATE TABLE choice CASCADE;
TRUNCATE TABLE department CASCADE;

-- Réinitialiser les séquences
ALTER SEQUENCE IF EXISTS interview_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS application_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS candidate_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS employee_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS job_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS qcm_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS department_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS employment_contract_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS publication_id_seq RESTART WITH 1;

-- ========================================
-- 2. DONNÉES DE BASE
-- ========================================

-- Départements
INSERT INTO department (name, description, created_at, updated_at) VALUES
('IT', 'Département Informatique', NOW(), NOW()),
('RH', 'Ressources Humaines', NOW(), NOW()),
('Commercial', 'Département Commercial', NOW(), NOW());

-- Jobs (Postes)
INSERT INTO job (title, description, required_degree, required_skills, department_id, created_at, updated_at) VALUES
('Développeur Java Senior', 'Développement applications Spring Boot', 'Master Informatique ou équivalent', 'Java, Spring, SQL, Git', 1, NOW(), NOW()),
('Développeur Full Stack', 'Développement Frontend et Backend', 'Master MIAGE ou équivalent', 'React, Node.js, Java, MongoDB', 1, NOW(), NOW()),
('Chef de Projet IT', 'Gestion de projets informatiques', 'Master Management de Projets', 'Agile, Scrum, Leadership, Communication', 1, NOW(), NOW());

-- QCM
INSERT INTO qcm (title, description, required_score, total_score, duration_minutes, created_at, updated_at) VALUES
('QCM Java Basique', 'Test de connaissances Java', 12.0, 20.0, 30, NOW(), NOW()),
('QCM Spring Boot', 'Test Spring Boot et microservices', 15.0, 25.0, 45, NOW(), NOW());

-- ========================================
-- 3. EMPLOYÉS (RECRUTEURS)
-- ========================================

INSERT INTO employee (employee_number, first_name, last_name, work_email, work_phone, birth_date, gender,
                      hire_date, base_salary, country, city, address, job_id, created_at, updated_at)
VALUES
-- ID: 1 - Recruteur principal (Marie Martin) - Poste RH (Chef de Projet IT temporairement)
('EMP001', 'Marie', 'Martin', 'marie.martin@company.com', '0123456789', '1985-05-15', 'F',
 '2020-01-15', 45000.00, 'France', 'Paris', '10 Rue du Commerce', 3, NOW(), NOW()),

-- ID: 2 - Recruteur IT (Pierre Dubois) - Développeur Java Senior
('EMP002', 'Pierre', 'Dubois', 'pierre.dubois@company.com', '0123456790', '1988-08-20', 'M',
 '2019-03-10', 48000.00, 'France', 'Lyon', '25 Avenue Tech', 1, NOW(), NOW()),

-- ID: 3 - Recruteur Commercial (Sophie Bernard) - Chef de Projet
('EMP003', 'Sophie', 'Bernard', 'sophie.bernard@company.com', '0123456791', '1990-11-25', 'F',
 '2021-06-01', 42000.00, 'France', 'Marseille', '15 Boulevard Business', 3, NOW(), NOW()),

-- ID: 4 - Chef RH (Thomas Petit) - Chef de Projet
('EMP004', 'Thomas', 'Petit', 'thomas.petit@company.com', '0123456792', '1982-03-12', 'M',
 '2018-09-15', 55000.00, 'France', 'Toulouse', '30 Rue Management', 3, NOW(), NOW());

-- ========================================
-- 4. CANDIDATS
-- ========================================

INSERT INTO candidate (first_name, last_name, email, phone, birth_date, address, city, country,
                       education_level, last_degree, years_experience, skills, created_at, updated_at)
VALUES
-- ID: 1 - Candidat pour test principal (Jean Dupont)
('Jean', 'Dupont', 'jean.dupont@email.com', '0612345678', '1990-05-15',
 '123 Rue de la Paix', 'Paris', 'France', 'Master', 'Master Informatique', 5,
 'Java, Spring Boot, MySQL, Git', NOW(), NOW()),

-- ID: 2 - Candidat pour test VISIO (Alice Durand)
('Alice', 'Durand', 'alice.durand@email.com', '0612345679', '1992-08-20',
 '456 Avenue des Champs', 'Lyon', 'France', 'Master', 'Master MIAGE', 3,
 'JavaScript, React, Node.js, MongoDB', NOW(), NOW()),

-- ID: 3 - Candidat pour test TELEPHONIQUE (Bob Moreau)
('Bob', 'Moreau', 'bob.moreau@email.com', '0612345680', '1988-12-10',
 '789 Boulevard Victor Hugo', 'Marseille', 'France', 'Licence', 'Licence Informatique', 7,
 'Python, Django, PostgreSQL, Docker', NOW(), NOW()),

-- ID: 4 - Candidat pour scénario complet (Claire Lefebvre)
('Claire', 'Lefebvre', 'claire.lefebvre@email.com', '0612345681', '1995-03-25',
 '321 Rue de la République', 'Toulouse', 'France', 'Master', 'Master Cloud Computing', 2,
 'AWS, Kubernetes, Terraform, Python', NOW(), NOW()),

-- ID: 5 - Candidat supplémentaire (David Rousseau)
('David', 'Rousseau', 'david.rousseau@email.com', '0612345682', '1991-07-18',
 '654 Avenue de la Liberté', 'Bordeaux', 'France', 'Ingénieur', 'Diplôme Ingénieur', 4,
 'Java, Angular, Spring, Microservices', NOW(), NOW());

-- ========================================
-- 5. PUBLICATIONS
-- ========================================

INSERT INTO publication (title, description, published_date, closing_date, number_of_positions, status, job_id, created_at, updated_at)
VALUES
('Développeur Java Senior - CDI', 'Poste de développeur Java expérimenté pour rejoindre notre équipe technique. Vous travaillerez sur des projets Spring Boot et microservices.',
 DATE '2025-10-01', DATE '2025-12-31', 2, 'Open', 1, NOW(), NOW()),

('Développeur Full Stack - CDI', 'Poste full stack React/Java pour développer nos applications web modernes. Vous interviendrez sur le frontend et le backend.',
 DATE '2025-10-05', DATE '2025-12-31', 1, 'Open', 2, NOW(), NOW());

-- ========================================
-- 6. CANDIDATURES (APPLICATIONS)
-- ========================================

INSERT INTO application (applied_at, status, document_score, score, comment,
                        candidate_id, publication_id, qcm_id, created_at, updated_at)
VALUES
-- ID: 1 - Candidature de Jean Dupont
(TIMESTAMP '2025-10-15 10:30:00', 'Received', 85.0, NULL, 'Bon profil technique', 1, 1, 1, NOW(), NOW()),

-- ID: 2 - Candidature de Alice Durand
(TIMESTAMP '2025-10-16 14:20:00', 'Received', 78.0, NULL, 'Profil junior intéressant', 2, 2, 2, NOW(), NOW()),

-- ID: 3 - Candidature de Bob Moreau
(TIMESTAMP '2025-10-17 09:15:00', 'Received', 90.0, NULL, 'Excellent profil senior', 3, 1, 1, NOW(), NOW()),

-- ID: 4 - Candidature de Claire Lefebvre
(TIMESTAMP '2025-10-18 11:45:00', 'Received', 82.0, NULL, 'Profil cloud prometteur', 4, 2, 2, NOW(), NOW());

-- ========================================
-- 7. ENTRETIENS EXISTANTS (pour tests de modification)
-- ========================================

INSERT INTO interview (candidate_id, interviewer_id, interview_date, start_time, duration_minutes,
                       interview_type, location, status, score, comment, application_id, created_at, updated_at)
VALUES
-- ID: 1 - Entretien pour tests de modification
(1, 1, DATE '2025-11-01', TIME '10:00:00', 60, 'PRESENTIEL',
 'Salle de réunion A - Bâtiment Principal', 'PLANIFIE', NULL, NULL, 1, NOW(), NOW()),

-- ID: 2 - Entretien déjà réalisé (pour statistiques)
(2, 2, DATE '2025-10-20', TIME '14:00:00', 45, 'VISIO',
 'https://meet.google.com/old-meeting', 'REALISE', 16.5,
 'Très bon candidat, compétences techniques solides', 2, NOW(), NOW()),

-- ID: 3 - Entretien annulé (pour statistiques)
(3, 1, DATE '2025-10-18', TIME '11:00:00', 30, 'TELEPHONIQUE',
 'Tel: +33 6 12 34 56 78', 'ANNULE', NULL,
 'Candidat a refusé le poste', 3, NOW(), NOW()),

-- ID: 4 - Entretien pour scénario complet (sera modifié dans les tests)
(4, 3, DATE '2025-11-15', TIME '14:00:00', 60, 'VISIO',
 'https://zoom.us/j/123456789', 'PLANIFIE', NULL, NULL, 4, NOW(), NOW());

-- ========================================
-- 8. ENTRETIENS SUPPLÉMENTAIRES POUR TESTS DE FILTRAGE
-- ========================================

INSERT INTO interview (candidate_id, interviewer_id, interview_date, start_time, duration_minutes,
                       interview_type, location, status, score, comment, application_id, created_at, updated_at)
VALUES
-- Entretiens pour la semaine du 01-07 novembre 2025 (sans application_id pour éviter les doublons)
(1, 1, DATE '2025-11-03', TIME '09:00:00', 60, 'PRESENTIEL', 'Salle B', 'PLANIFIE', NULL, NULL, NULL, NOW(), NOW()),
(2, 1, DATE '2025-11-04', TIME '10:30:00', 45, 'VISIO', 'https://meet.google.com/test1', 'PLANIFIE', NULL, NULL, NULL, NOW(), NOW()),
(3, 2, DATE '2025-11-05', TIME '14:00:00', 60, 'PRESENTIEL', 'Salle C', 'PLANIFIE', NULL, NULL, NULL, NOW(), NOW()),
(4, 2, DATE '2025-11-06', TIME '11:00:00', 30, 'TELEPHONIQUE', 'Tel: +33 6 11 22 33 44', 'PLANIFIE', NULL, NULL, NULL, NOW(), NOW()),

-- Entretiens réalisés (pour statistiques - sans application_id)
(5, 1, DATE '2025-10-25', TIME '10:00:00', 60, 'PRESENTIEL', 'Salle A', 'REALISE', 15.0, 'Bon candidat', NULL, NOW(), NOW()),
(1, 2, DATE '2025-10-26', TIME '15:00:00', 45, 'VISIO', 'https://meet.google.com/test2', 'REALISE', 17.5, 'Excellent profil', NULL, NOW(), NOW()),

-- Entretiens VISIO planifiés (pour test de filtrage par type - sans application_id)
(2, 3, DATE '2025-11-08', TIME '14:00:00', 60, 'VISIO', 'https://zoom.us/j/111111', 'PLANIFIE', NULL, NULL, NULL, NOW(), NOW()),
(3, 3, DATE '2025-11-09', TIME '16:00:00', 45, 'VISIO', 'https://teams.microsoft.com/meeting1', 'PLANIFIE', NULL, NULL, NULL, NOW(), NOW());

-- ========================================
-- 9. VÉRIFICATIONS
-- ========================================

-- Afficher le résumé des données créées
SELECT 'Départements créés' AS info, COUNT(*)::TEXT AS total FROM department
UNION ALL
SELECT 'Jobs créés', COUNT(*)::TEXT FROM job
UNION ALL
SELECT 'QCM créés', COUNT(*)::TEXT FROM qcm
UNION ALL
SELECT 'Employés créés', COUNT(*)::TEXT FROM employee
UNION ALL
SELECT 'Candidats créés', COUNT(*)::TEXT FROM candidate
UNION ALL
SELECT 'Publications créées', COUNT(*)::TEXT FROM publication
UNION ALL
SELECT 'Candidatures créées', COUNT(*)::TEXT FROM application
UNION ALL
SELECT 'Entretiens créés', COUNT(*)::TEXT FROM interview;

-- ========================================
-- 10. REQUÊTES DE VÉRIFICATION DÉTAILLÉES
-- ========================================

-- Vérifier les employés (recruteurs)
SELECT
    id,
    employee_number,
    first_name || ' ' || last_name AS name,
    work_email,
    job_id,
    city
FROM employee
ORDER BY id;

-- Vérifier les candidats
SELECT
    id,
    first_name || ' ' || last_name AS name,
    email,
    years_experience,
    skills
FROM candidate
ORDER BY id;

-- Vérifier les candidatures
SELECT
    a.id,
    c.first_name || ' ' || c.last_name AS candidate,
    a.status,
    a.document_score,
    p.title AS poste
FROM application a
JOIN candidate c ON a.candidate_id = c.id
LEFT JOIN publication p ON a.publication_id = p.id
ORDER BY a.id;

-- Vérifier les entretiens créés
SELECT
    i.id,
    c.first_name || ' ' || c.last_name AS candidat,
    e.first_name || ' ' || e.last_name AS recruteur,
    i.interview_date AS date,
    i.start_time AS heure,
    i.duration_minutes AS durée_min,
    i.interview_type AS type,
    i.status AS statut,
    i.score AS note,
    LEFT(i.location, 50) AS lieu
FROM interview i
JOIN candidate c ON i.candidate_id = c.id
JOIN employee e ON i.interviewer_id = e.id
ORDER BY i.interview_date, i.start_time;

-- Statistiques par statut
SELECT
    status,
    COUNT(*) AS nombre,
    STRING_AGG(id::TEXT, ', ' ORDER BY id) AS ids
FROM interview
GROUP BY status
ORDER BY status;

-- Statistiques par type
SELECT
    interview_type AS type,
    COUNT(*) AS nombre,
    STRING_AGG(id::TEXT, ', ' ORDER BY id) AS ids
FROM interview
GROUP BY interview_type
ORDER BY interview_type;

-- Statistiques par recruteur
SELECT
    e.first_name || ' ' || e.last_name AS recruteur,
    COUNT(i.id) AS nb_entretiens,
    SUM(CASE WHEN i.status = 'PLANIFIE' THEN 1 ELSE 0 END) AS planifiés,
    SUM(CASE WHEN i.status = 'REALISE' THEN 1 ELSE 0 END) AS réalisés,
    SUM(CASE WHEN i.status = 'ANNULE' THEN 1 ELSE 0 END) AS annulés
FROM employee e
LEFT JOIN interview i ON e.id = i.interviewer_id
GROUP BY e.id, e.first_name, e.last_name
ORDER BY nb_entretiens DESC;

-- ========================================
-- INFORMATIONS POUR LES TESTS
-- ========================================

SELECT '
========================================
DONNÉES DE TEST CRÉÉES AVEC SUCCÈS
========================================

VARIABLES POUR test_entretien.http:
- @candidateId = 1 (Jean Dupont)
- @interviewerId = 1 (Marie Martin)
- @interviewId = 1 (Entretien modifiable)
- @applicationId = 1

CANDIDATS DISPONIBLES:
- ID 1: Jean Dupont (jean.dupont@email.com)
- ID 2: Alice Durand (alice.durand@email.com)
- ID 3: Bob Moreau (bob.moreau@email.com)
- ID 4: Claire Lefebvre (claire.lefebvre@email.com)
- ID 5: David Rousseau (david.rousseau@email.com)

RECRUTEURS DISPONIBLES:
- ID 1: Marie Martin (RH)
- ID 2: Pierre Dubois (IT)
- ID 3: Sophie Bernard (Commercial)
- ID 4: Thomas Petit (Chef RH)

ENTRETIENS EXISTANTS:
- ID 1: PLANIFIE (pour tests de modification)
- ID 2: REALISE (pour statistiques)
- ID 3: ANNULE (pour statistiques)
- ID 4: PLANIFIE (pour scénario complet)
- IDs 5-12: Divers statuts et types

TOUS LES ENDPOINTS DE test_entretien.http
PEUVENT MAINTENANT ÊTRE TESTÉS !

========================================
' AS info;

