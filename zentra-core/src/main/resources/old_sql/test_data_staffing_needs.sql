-- ===========================================
-- DONNÉES DE TEST - GESTION DES BESOINS EN PERSONNEL
-- ===========================================

-- Insérer des départements de test
INSERT INTO department (department_id, name, description, annual_budget, created_at, updated_at)
VALUES 
    (101, 'Département IT', 'Technologies de l''information et développement', 800000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (102, 'Ressources Humaines', 'Gestion du personnel et recrutement', 450000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (103, 'Marketing & Communication', 'Marketing digital et communication corporate', 600000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (department_id) DO NOTHING;

-- Insérer des postes de test
INSERT INTO job (job_id, title, description, required_degree, required_skills, department_id, created_date, updated_at)
VALUES 
    (201, 'Développeur Full Stack', 'Développeur avec expertise frontend et backend', 'Master Informatique', 'React, TypeScript, Spring Boot, PostgreSQL, Docker', 101, CURRENT_DATE, CURRENT_DATE),
    (202, 'Architecte Cloud', 'Architecte spécialisé dans les solutions cloud', 'Master + 7 ans exp', 'AWS, Azure, Kubernetes, Terraform, Microservices', 101, CURRENT_DATE, CURRENT_DATE),
    (203, 'Chargé de Recrutement', 'Recruteur spécialisé en profils IT', 'Licence RH', 'ATS, LinkedIn Recruiter, Entretiens techniques', 102, CURRENT_DATE, CURRENT_DATE),
    (204, 'Community Manager', 'Gestionnaire de communautés et réseaux sociaux', 'Licence Communication', 'Réseaux sociaux, Content marketing, Analytics', 103, CURRENT_DATE, CURRENT_DATE)
ON CONFLICT (job_id) DO NOTHING;

-- Insérer des employés de test (pour requested_by)
INSERT INTO employee (employee_id, first_name, last_name, work_email, work_phone, hire_date, job_id, base_salary, birth_date, employee_number)
VALUES 
    (301, 'Marie', 'Dubois', 'marie.dubois@zentra.com', '0601020304', '2020-01-15', 201, 65000.00, '1990-05-20', 'EMP001'),
    (302, 'Pierre', 'Martin', 'pierre.martin@zentra.com', '0612345678', '2019-06-10', 203, 48000.00, '1988-11-15', 'EMP002'),
    (303, 'Sophie', 'Bernard', 'sophie.bernard@zentra.com', '0623456789', '2021-03-20', 204, 42000.00, '1992-03-08', 'EMP003')
ON CONFLICT (employee_id) DO NOTHING;

-- Insérer des besoins en personnel de test
INSERT INTO staffing_need (title, description, number_of_positions, priority, status, 
                           required_start_date, budget_allocated, justification,
                           department_id, job_id, requested_by, created_at, updated_at)
VALUES 
    -- Besoin urgent en cours
    ('Développeur Full Stack Senior', 
     'Besoin immédiat de développeurs full stack pour un projet de refonte de notre plateforme web. Expertise React et Spring Boot requise.',
     3, 'High', 'In Progress',
     '2025-12-01', 210000.00,
     'Projet stratégique de transformation digitale avec deadline serrée. Budget validé par la direction.',
     101, 201, 301,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Besoin ouvert
    ('Architecte Cloud Azure', 
     'Recherche d''un architecte cloud avec expertise Azure pour accompagner notre migration vers le cloud.',
     1, 'High', 'Open',
     '2026-01-15', 95000.00,
     'Migration cloud prévue pour Q1 2026. Besoin d''expertise pointue pour éviter les erreurs coûteuses.',
     101, 202, 301,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Besoin rempli
    ('Chargé de Recrutement IT', 
     'Recruteur spécialisé pour gérer le volume croissant de recrutements techniques.',
     1, 'Medium', 'Fulfilled',
     '2025-10-01', 48000.00,
     'Volume de recrutements IT en forte augmentation (+40%). Poste déjà pourvu.',
     102, 203, 302,
     '2025-09-01 10:00:00', CURRENT_TIMESTAMP),
    
    -- Besoin annulé
    ('Développeur Mobile', 
     'Développeur mobile iOS/Android pour application interne.',
     2, 'Low', 'Cancelled',
     '2025-11-01', 110000.00,
     'Projet d''application mobile reporté à 2026 suite à réallocation budgétaire.',
     101, 201, 301,
     '2025-08-15 14:30:00', CURRENT_TIMESTAMP),
    
    -- Besoin moyen priorité ouvert
    ('Community Manager', 
     'Community manager pour animer nos réseaux sociaux et engager notre communauté.',
     1, 'Medium', 'Open',
     '2026-02-01', 42000.00,
     'Renforcement de notre présence digitale et engagement client.',
     103, 204, 303,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Besoin faible priorité ouvert
    ('Stagiaire Développement Web', 
     'Stage de 6 mois pour support sur projets web internes.',
     2, 'Low', 'Open',
     '2026-03-01', 8000.00,
     'Accompagnement des équipes sur tâches de développement simples.',
     101, 201, 301,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Mettre à jour les séquences pour éviter les conflits
SELECT setval('department_id_seq', 200, true);
SELECT setval('job_id_seq', 300, true);
SELECT setval('employee_id_seq', 400, true);
SELECT setval('staffing_need_id_seq', 100, true);

-- Afficher un résumé
SELECT 
    'RÉSUMÉ DES DONNÉES DE TEST' as info;

SELECT 
    COUNT(*) as total_besoins,
    SUM(number_of_positions) as total_postes_demandes,
    SUM(budget_allocated) as budget_total
FROM staffing_need;

SELECT 
    status,
    COUNT(*) as nombre,
    SUM(number_of_positions) as postes,
    SUM(budget_allocated) as budget
FROM staffing_need
GROUP BY status
ORDER BY 
    CASE status
        WHEN 'Open' THEN 1
        WHEN 'In Progress' THEN 2
        WHEN 'Fulfilled' THEN 3
        WHEN 'Cancelled' THEN 4
    END;

SELECT 
    priority,
    COUNT(*) as nombre,
    SUM(budget_allocated) as budget
FROM staffing_need
GROUP BY priority
ORDER BY 
    CASE priority
        WHEN 'High' THEN 1
        WHEN 'Medium' THEN 2
        WHEN 'Low' THEN 3
    END;
