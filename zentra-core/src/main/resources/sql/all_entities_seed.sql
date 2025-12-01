-- Script simple de données de test pour toutes les entités
-- TRUNCATE + INSERTs basiques
-- A utiliser uniquement en environnement de développement / test

-- 1) Nettoyage (ordre respectant les FK)
TRUNCATE TABLE answer CASCADE;
TRUNCATE TABLE attempt CASCADE;
TRUNCATE TABLE choice CASCADE;
TRUNCATE TABLE question CASCADE;
TRUNCATE TABLE qcm CASCADE;
TRUNCATE TABLE application CASCADE;
TRUNCATE TABLE token CASCADE;
TRUNCATE TABLE interview CASCADE;
TRUNCATE TABLE publication CASCADE;
TRUNCATE TABLE employment_contract CASCADE;
TRUNCATE TABLE employee CASCADE;
TRUNCATE TABLE candidate CASCADE;
TRUNCATE TABLE job CASCADE;
TRUNCATE TABLE department CASCADE;

-- 2) Réinitialisation des séquences (si elles existent)
ALTER SEQUENCE IF EXISTS department_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS job_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS candidate_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS employee_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS employment_contract_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS publication_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS application_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS qcm_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS question_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS choice_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS attempt_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS response_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS interview_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS token_id_seq RESTART WITH 1;

-- 3) INSERTS simples

-- Departements
INSERT INTO department (name, description, annual_budget)
VALUES
  ('Informatique', 'Département développement & infrastructure', 500000.00),
  ('Ressources Humaines', 'Gestion du personnel', 120000.00),
  ('Commercial', 'Ventes et partenariats', 200000.00);

-- Jobs (référencent department_id 1..3)
INSERT INTO job (title, description, required_degree, required_skills, department_id)
VALUES
  ('Développeur Backend', 'Backend Java/Spring', 'Licence/Master', 'Java,Spring,SQL', 1),
  ('Développeur Frontend', 'Frontend React/TypeScript', 'Licence', 'React,TypeScript,HTML,CSS', 1),
  ('Chargé RH', 'Recrutement & administration', 'Licence', 'Recrutement,Paie', 2),
  ('Commercial', 'Gestion clients', 'Licence', 'Vente,CRM', 3);

-- Publications (liées aux jobs)
INSERT INTO publication (title, description, published_date, closing_date, number_of_positions, status, job_id)
VALUES
  ('Backend Java - CDI', 'Poste backend Java pour projet microservices', '2025-10-01', '2025-12-31', 2, 'Open', 1),
  ('Frontend React - CDI', 'Développeur frontend React', '2025-10-05', '2025-12-31', 1, 'Open', 2),
  ('Chargé RH - CDD', 'Mission RH', '2025-09-01', '2025-11-30', 1, 'Open', 3);

-- Candidates
INSERT INTO candidate (last_name, first_name, email, phone, birth_date, country, years_experience)
VALUES
  ('Dupont', 'Jean', 'jean.dupont@email.com', '+33123456789', '1990-05-15', 'France', 5),
  ('Durand', 'Alice', 'alice.durand@email.com', '+33698765432', '1992-08-20', 'France', 3),
  ('Moreau', 'Bob', 'bob.moreau@email.com', '+33611223344', '1988-12-10', 'France', 7);

-- Applications (candidatures)
INSERT INTO application (applied_at, status, document_score, comment, candidate_id, publication_id)
VALUES
  ('2025-10-15 10:30:00', 'Received', 85.0, 'Bonne expérience backend', 1, 1),
  ('2025-10-16 14:20:00', 'Received', 78.0, 'Profil frontend junior', 2, 2);

-- Employees
INSERT INTO employee (employee_number, last_name, first_name, work_email, birth_date, gender, hire_date, base_salary, job_id)
VALUES
  ('EMP001', 'Martin', 'Marie', 'marie.martin@company.com', '1985-05-15', 'F', '2020-01-15', 45000.00, 3),
  ('EMP002', 'Dubois', 'Pierre', 'pierre.dubois@company.com', '1988-08-20', 'M', '2019-03-10', 48000.00, 1);

-- Employment contracts (liés aux employees)
INSERT INTO employment_contract (employee_id, contract_number, start_date, gross_salary, weekly_hours)
VALUES
  (1, 'C-2023-001', '2023-01-01', 45000.00, 35.0),
  (2, 'C-2022-002', '2022-06-01', 48000.00, 35.0);

-- QCM
INSERT INTO qcm (title, description, date, duration_minutes, total_score, required_score)
VALUES
  ('QCM Java', 'Test de connaissances Java', '2025-10-01', 30, 20.0, 12.0),
  ('QCM Frontend', 'Test React', '2025-10-02', 30, 20.0, 12.0);

-- Questions
INSERT INTO question (libelle, required, score, qcm_id)
VALUES
  ('Qu''est-ce que la JVM ?', true, 5.0, 1),
  ('Qu''est-ce que React ?', true, 5.0, 2);

-- Choices
INSERT INTO choice (libelle, score, correct, question_id)
VALUES
  ('Machine virtuelle Java', 5.0, true, 1),
  ('Bibliothèque UI', 5.0, true, 2);

-- Attempts (tentatives de QCM) - lier application_id 1..2
INSERT INTO attempt (qcm_id, application_id, obtained_score)
VALUES
  (1, 1, 16.0),
  (2, 2, 14.0);

-- Responses
INSERT INTO response (attempt_id, choice_id)
VALUES
  (1, 1),
  (2, 2);

-- Interviews (liés à application_id)
INSERT INTO interview (interview_type, interview_date, start_time, duration_minutes, interviewer_id, report, score, application_id)
VALUES
  ('Phone', '2025-11-01', '10:00:00', 60, 1, 'Entretien initial', NULL, 1),
  ('Video', '2025-10-20', '14:00:00', 45, 2, 'Entretien technique', 16.5, 2);

-- Token (optionnel si la table existe)
-- INSERT INTO token (value, active, expiration_date, application_id) VALUES ('token123', true, '2026-01-01', 1);

-- Vérifications rapides
SELECT 'SUMMARY' AS tag,
  (SELECT COUNT(*) FROM department) AS departments,
  (SELECT COUNT(*) FROM job) AS jobs,
  (SELECT COUNT(*) FROM publication) AS publications,
  (SELECT COUNT(*) FROM candidate) AS candidates,
  (SELECT COUNT(*) FROM application) AS applications;

