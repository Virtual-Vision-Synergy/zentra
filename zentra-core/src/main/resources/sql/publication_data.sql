-- Données de test très simples pour Publications
-- A exécuter après la création des tables (table_rh.sql)

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
TRUNCATE TABLE staffing_need CASCADE;
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
ALTER SEQUENCE IF EXISTS staffing_need_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS token_id_seq RESTART WITH 1;
-- 1) Départements
INSERT INTO department (name, description, annual_budget)
VALUES
  ('Informatique', 'Département développement & infrastructure', 500000.00),
  ('Ressources Humaines', 'Gestion du personnel', 150000.00);

-- 2) Jobs (postes) — on utilise department_id = 1 et 2 (séquences supposées démarrer à 1)
INSERT INTO job (title, description, required_degree, required_skills, department_id)
VALUES
  ('Développeur Backend Java', 'Développement d''API en Spring Boot', 'Licence/Master', 'Java,Spring Boot,SQL', 1),
  ('Développeur Frontend React', 'UI avec React + TypeScript', 'Licence', 'React,TypeScript,HTML,CSS', 1),
  ('Chargé RH', 'Recrutement & administration du personnel', 'Licence', 'Recrutement,Paie,Relations Sociales', 2);

-- 3) StaffingNeeds (chaque besoin référence un Job existant)
INSERT INTO staffing_need (
  title, description, number_of_positions, priority, status, required_start_date,
  budget_allocated, justification, department_id, job_id, requested_by
) VALUES
  ('Besoin Backend Java', 'Renfort backend pour projets microservices', 2, 'High', 'Open', '2025-02-01', 100000.00, 'Croissance d''activité', 1, 1, NULL),
  ('Besoin Frontend React', 'Refonte UI et amélioration UX', 1, 'Medium', 'Open', '2025-02-10', 70000.00, 'Lancement d''une nouvelle interface', 1, 2, NULL),
  ('Besoin Chargé RH', 'Appui recrutement et administration du personnel', 1, 'Low', 'Open', '2024-11-15', 50000.00, 'Période de fort volume', 2, 3, NULL);

-- 4) Publications (créées à partir des StaffingNeeds; job_id dérivé du besoin)
INSERT INTO publication (title, description, published_date, closing_date, number_of_positions, status, job_id)
VALUES
  (
    'Backend Java - API & Microservices',
    'Poste pour développeur backend expérimenté sur Spring Boot et microservices.',
    '2025-01-15', '2025-02-15',
    2, 'Open',
    (SELECT sn.job_id FROM staffing_need sn WHERE sn.title = 'Besoin Backend Java' ORDER BY sn.id DESC LIMIT 1)
  ),
  (
    'Frontend React - UI/UX',
    'Recherche développeur frontend maîtrisant React et TypeScript.',
    '2025-02-01', '2025-03-01',
    1, 'Open',
    (SELECT sn.job_id FROM staffing_need sn WHERE sn.title = 'Besoin Frontend React' ORDER BY sn.id DESC LIMIT 1)
  ),
  (
    'Chargé RH - Recrutement',
    'Mission RH: tri des candidatures et entretiens.',
    '2024-12-01', '2025-01-10',
    1, 'Closed',
    (SELECT sn.job_id FROM staffing_need sn WHERE sn.title = 'Besoin Chargé RH' ORDER BY sn.id DESC LIMIT 1)
  );

-- 5) Applications (lier un candidat à une publication)
INSERT INTO application (applied_at, status, candidate_id, publication_id)
VALUES
  ('2025-01-20 10:00:00', 'Received', 1, 1),
  ('2025-02-05 14:30:00', 'Under Review', 2, 2);

-- Note: Ce script suppose une base vide et des séquences démarrant à 1.
-- Si votre schéma utilise d'autres noms de colonnes (ex: createdAt), adaptez en conséquence.
