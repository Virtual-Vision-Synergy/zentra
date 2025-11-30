 -- Script de vérification et insertion de données de test pour les employés

-- ========================================
-- 1. VÉRIFICATION DES DONNÉES EXISTANTES
-- ========================================

-- Compter les employés
SELECT COUNT(*) as "Nombre d'employés" FROM employee;

-- Compter les jobs
SELECT COUNT(*) as "Nombre de jobs" FROM job;

-- Lister les employés avec leur poste
SELECT
    e.id,
    e.first_name || ' ' || e.last_name as "Nom complet",
    j.title as "Poste",
    e.base_salary as "Salaire"
FROM employee e
LEFT JOIN job j ON e.job_id = j.id
ORDER BY e.last_name, e.first_name
LIMIT 10;

-- ========================================
-- 2. INSERTION DE DONNÉES DE TEST (SI VIDE)
-- ========================================
-- Décommentez les sections ci-dessous si vous voulez insérer des données de test

/*
-- Insérer des jobs de test
INSERT INTO job (title, description, min_salary, max_salary, department)
VALUES
    ('Développeur Full Stack', 'Développement d''applications web', 35000, 60000, 'IT'),
    ('Chef de Projet', 'Gestion de projets informatiques', 50000, 80000, 'Management'),
    ('Développeur Frontend', 'Développement d''interfaces utilisateur', 30000, 50000, 'IT'),
    ('Développeur Backend', 'Développement d''APIs et services', 35000, 55000, 'IT'),
    ('Data Analyst', 'Analyse de données et reporting', 40000, 65000, 'Data'),
    ('DevOps Engineer', 'Infrastructure et déploiement', 45000, 70000, 'IT'),
    ('UX/UI Designer', 'Design d''interfaces utilisateur', 35000, 55000, 'Design'),
    ('Product Owner', 'Gestion de produit', 50000, 75000, 'Management'),
    ('Testeur QA', 'Tests et qualité logicielle', 30000, 45000, 'IT'),
    ('Business Analyst', 'Analyse métier', 40000, 60000, 'Business')
ON CONFLICT DO NOTHING;

-- Récupérer les IDs des jobs (adapter selon vos données)
DO $$
DECLARE
    job_dev_fullstack_id INTEGER;
    job_chef_projet_id INTEGER;
    job_dev_frontend_id INTEGER;
    job_dev_backend_id INTEGER;
    job_data_analyst_id INTEGER;
BEGIN
    -- Récupérer les IDs
    SELECT id INTO job_dev_fullstack_id FROM job WHERE title = 'Développeur Full Stack' LIMIT 1;
    SELECT id INTO job_chef_projet_id FROM job WHERE title = 'Chef de Projet' LIMIT 1;
    SELECT id INTO job_dev_frontend_id FROM job WHERE title = 'Développeur Frontend' LIMIT 1;
    SELECT id INTO job_dev_backend_id FROM job WHERE title = 'Développeur Backend' LIMIT 1;
    SELECT id INTO job_data_analyst_id FROM job WHERE title = 'Data Analyst' LIMIT 1;

    -- Insérer des employés de test
    INSERT INTO employee (
        employee_number, first_name, last_name,
        work_email, work_phone,
        birth_date, gender, hire_date,
        base_salary, job_id,
        country, address, city
    ) VALUES
        ('EMP001', 'Jean', 'Dupont', 'jean.dupont@zentra.mg', '+261 34 12 345 67',
         '1990-05-15', 'M', '2020-01-10',
         45000.0, job_dev_fullstack_id,
         'Madagascar', '123 Avenue de l''Indépendance', 'Antananarivo'),

        ('EMP002', 'Marie', 'Martin', 'marie.martin@zentra.mg', '+261 34 23 456 78',
         '1988-08-22', 'F', '2019-03-15',
         55000.0, job_chef_projet_id,
         'Madagascar', '45 Rue de la République', 'Antananarivo'),

        ('EMP003', 'Pierre', 'Durand', 'pierre.durand@zentra.mg', '+261 34 34 567 89',
         '1992-11-30', 'M', '2021-06-01',
         40000.0, job_dev_frontend_id,
         'Madagascar', '78 Boulevard Ranavalona', 'Antananarivo'),

        ('EMP004', 'Sophie', 'Bernard', 'sophie.bernard@zentra.mg', '+261 34 45 678 90',
         '1991-03-12', 'F', '2021-09-15',
         42000.0, job_dev_backend_id,
         'Madagascar', '12 Rue Rainitovo', 'Antananarivo'),

        ('EMP005', 'Lucas', 'Petit', 'lucas.petit@zentra.mg', '+261 34 56 789 01',
         '1989-07-08', 'M', '2020-11-20',
         48000.0, job_data_analyst_id,
         'Madagascar', '90 Avenue de France', 'Antananarivo')
    ON CONFLICT (employee_number) DO NOTHING;

    RAISE NOTICE 'Données de test insérées avec succès !';
END $$;
*/

-- ========================================
-- 3. VÉRIFICATION APRÈS INSERTION
-- ========================================

-- Compter à nouveau
SELECT COUNT(*) as "Nombre d'employés après insertion" FROM employee;

-- Lister tous les employés avec leurs postes
SELECT
    e.id,
    e.employee_number as "Matricule",
    e.first_name || ' ' || e.last_name as "Nom complet",
    j.title as "Poste",
    e.base_salary as "Salaire",
    e.hire_date as "Date d'embauche"
FROM employee e
LEFT JOIN job j ON e.job_id = j.id
ORDER BY e.hire_date DESC;

-- ========================================
-- 4. TESTS DE REQUÊTE (même que l'API)
-- ========================================

-- Requête exactement comme dans DataController.java
SELECT e.id,
       e.first_name as firstName,
       e.last_name as lastName,
       j.title as position,
       e.base_salary as salary
FROM employee e
LEFT JOIN job j ON e.job_id = j.id
ORDER BY e.last_name, e.first_name;

-- ========================================
-- NOTES
-- ========================================
-- Si vous voyez des données ici mais pas dans l'API :
-- 1. Vérifiez que le backend est redémarré
-- 2. Vérifiez les logs du backend pour les erreurs
-- 3. Testez avec : curl http://localhost:8080/api/hr/employees

