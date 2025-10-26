-- ===========================================
-- DONNÉES DE TEST SIMPLIFIÉES (100 % FONCTIONNELLES)
-- ===========================================

-- 1. Insérer un département
INSERT INTO department (name, description, annual_budget, created_at, updated_at)
VALUES (
    'Département IT',
    'Technologies de l''information',
    800000.00,
    CURRENT_DATE,
    CURRENT_DATE
);

-- 2. Insérer un poste
INSERT INTO job (
    title, description, required_degree, required_skills,
    department_id, created_at, updated_at
)
VALUES (
    'Développeur Full Stack',
    'Développeur frontend et backend',
    'Master',
    'React, Spring Boot, PostgreSQL',
    (SELECT id FROM department WHERE name = 'Département IT'),
    CURRENT_DATE,
    CURRENT_DATE
);

-- 3. Insérer un employé
INSERT INTO employee (
    employee_number, first_name, last_name, work_email, work_phone,
    birth_date, hire_date, base_salary, job_id,
    country, city, address, created_at, updated_at
)
VALUES (
    'EMP001', 'Marie', 'Dubois', 'marie.dubois@zentra.com', '0601020304',
    '1990-05-20', '2020-01-15', 65000.00,
    (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
    'France', 'Paris', '10 Rue de la Paix',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- 4. Insérer des besoins en personnel
INSERT INTO staffing_need (
    title, description, number_of_positions, priority, status,
    required_start_date, budget_allocated, justification,
    department_id, job_id, requested_by, created_at, updated_at
)
VALUES 
    ('Développeur Full Stack Senior',
     'Besoin immédiat de développeurs full stack pour projet de refonte',
     3, 'High', 'In Progress',
     '2025-12-01', 210000.00,
     'Projet stratégique de transformation digitale',
     (SELECT id FROM department WHERE name = 'Département IT'),
     (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
     (SELECT id FROM employee WHERE employee_number = 'EMP001'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Architecte Cloud Azure',
     'Architecte cloud avec expertise Azure pour migration',
     1, 'High', 'Open',
     '2026-01-15', 95000.00,
     'Migration cloud prévue pour Q1 2026',
     (SELECT id FROM department WHERE name = 'Département IT'),
     (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
     (SELECT id FROM employee WHERE employee_number = 'EMP001'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Développeur Backend Java',
     'Développeur Java avec expertise microservices',
     2, 'Medium', 'Open',
     '2026-02-01', 120000.00,
     'Extension de l''équipe backend',
     (SELECT id FROM department WHERE name = 'Département IT'),
     (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
     (SELECT id FROM employee WHERE employee_number = 'EMP001'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Stagiaire Développement',
     'Stage de 6 mois en développement web',
     1, 'Low', 'Open',
     '2026-03-01', 8000.00,
     'Support sur projets internes',
     (SELECT id FROM department WHERE name = 'Département IT'),
     (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
     (SELECT id FROM employee WHERE employee_number = 'EMP001'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Lead Developer',
     'Lead developer pour diriger l''équipe technique',
     1, 'High', 'Fulfilled',
     '2025-10-01', 85000.00,
     'Poste de lead pour encadrer l''équipe',
     (SELECT id FROM department WHERE name = 'Département IT'),
     (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
     (SELECT id FROM employee WHERE employee_number = 'EMP001'),
     '2025-09-01 10:00:00', CURRENT_TIMESTAMP),

    ('Développeur Mobile',
     'Développeur mobile iOS/Android',
     1, 'Low', 'Cancelled',
     '2026-04-01', 70000.00,
     'Projet reporté à 2027',
     (SELECT id FROM department WHERE name = 'Département IT'),
     (SELECT id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1),
     (SELECT id FROM employee WHERE employee_number = 'EMP001'),
     '2025-08-15 14:30:00', CURRENT_TIMESTAMP);

-- ===========================================
-- RÉSUMÉ
-- ===========================================

SELECT 'DONNÉES INSÉRÉES AVEC SUCCÈS !' AS message;

SELECT 
    COUNT(*) AS total_besoins,
    SUM(number_of_positions) AS total_postes,
    SUM(budget_allocated) AS budget_total
FROM staffing_need;

SELECT 
    status,
    COUNT(*) AS nombre
FROM staffing_need
GROUP BY status;
