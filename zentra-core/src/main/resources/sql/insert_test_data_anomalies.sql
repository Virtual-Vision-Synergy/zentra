-- Script d'insertion de données de test pour la détection d'anomalies
-- À exécuter : psql -U postgres -d zentra -f insert_test_data_anomalies.sql

-- ========================================
-- 1. CRÉER DES EMPLOYÉS DE TEST SI NÉCESSAIRE
-- ========================================

-- Vérifier si des employés existent déjà
DO $$
DECLARE
    employee_count INTEGER;
    job_dev_id INTEGER;
    job_manager_id INTEGER;
BEGIN
    SELECT COUNT(*) INTO employee_count FROM employee;

    IF employee_count < 5 THEN
        -- Créer des départements si nécessaire
        INSERT INTO department (name, description)
        VALUES ('IT', 'Technologies de l''information')
        ON CONFLICT (name) DO NOTHING;

        INSERT INTO department (name, description)
        VALUES ('Management', 'Direction et gestion')
        ON CONFLICT (name) DO NOTHING;

        -- Créer des jobs si nécessaire
        INSERT INTO job (title, description, department_id)
        SELECT 'Développeur', 'Développeur logiciel', d.id
        FROM department d WHERE d.name = 'IT'
        ON CONFLICT DO NOTHING;

        INSERT INTO job (title, description, department_id)
        SELECT 'Manager', 'Chef d''équipe', d.id
        FROM department d WHERE d.name = 'Management'
        ON CONFLICT DO NOTHING;

        SELECT id INTO job_dev_id FROM job WHERE title = 'Développeur' LIMIT 1;
        SELECT id INTO job_manager_id FROM job WHERE title = 'Manager' LIMIT 1;

        -- Créer des employés de test
        INSERT INTO employee (employee_number, first_name, last_name, work_email, birth_date, hire_date, base_salary, job_id, country)
        VALUES
            ('EMP001', 'Jean', 'Rakoto', 'jean.rakoto@zentra.mg', '1990-01-15', '2020-01-10', 450000, job_dev_id, 'Madagascar'),
            ('EMP002', 'Marie', 'Rasoa', 'marie.rasoa@zentra.mg', '1988-05-20', '2019-06-15', 520000, job_manager_id, 'Madagascar'),
            ('EMP003', 'Pierre', 'Randria', 'pierre.randria@zentra.mg', '1992-08-10', '2021-03-01', 380000, job_dev_id, 'Madagascar'),
            ('EMP004', 'Sophie', 'Rabe', 'sophie.rabe@zentra.mg', '1995-12-05', '2022-09-15', 420000, job_dev_id, 'Madagascar'),
            ('EMP005', 'Lucas', 'Andria', 'lucas.andria@zentra.mg', '1987-03-22', '2018-11-20', 550000, job_manager_id, 'Madagascar')
        ON CONFLICT (employee_number) DO NOTHING;

        RAISE NOTICE 'Employés de test créés';
    END IF;
END $$;

-- ========================================
-- 2. ANOMALIES DE POINTAGE (HORAIRES ANORMAUX)
-- ========================================

-- Table attendance (si elle existe)
-- Créer la table si nécessaire
CREATE TABLE IF NOT EXISTS attendance (
    id BIGSERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    status VARCHAR(20),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Nettoyer les anciennes données de test
DELETE FROM attendance WHERE date >= CURRENT_DATE - INTERVAL '30 days';

-- Insérer des données normales et anormales
DO $$
DECLARE
    emp_record RECORD;
    test_date DATE;
    i INTEGER;
BEGIN
    -- Pour chaque employé
    FOR emp_record IN SELECT id FROM employee LIMIT 5 LOOP

        -- Données des 30 derniers jours
        FOR i IN 0..29 LOOP
            test_date := CURRENT_DATE - i;

            -- Ignorer les weekends
            IF EXTRACT(DOW FROM test_date) NOT IN (0, 6) THEN

                -- 70% de pointages normaux
                IF RANDOM() < 0.7 THEN
                    INSERT INTO attendance (employee_id, date, check_in, check_out, status)
                    VALUES (
                        emp_record.id,
                        test_date,
                        TIME '08:00:00' + (RANDOM() * INTERVAL '30 minutes'),
                        TIME '17:00:00' + (RANDOM() * INTERVAL '30 minutes'),
                        'PRESENT'
                    );

                -- 10% d'arrivées très tardives (ANOMALIE)
                ELSIF RANDOM() < 0.8 THEN
                    INSERT INTO attendance (employee_id, date, check_in, check_out, status, notes)
                    VALUES (
                        emp_record.id,
                        test_date,
                        TIME '11:00:00' + (RANDOM() * INTERVAL '2 hours'),
                        TIME '17:00:00',
                        'LATE',
                        'Retard excessif - ANOMALIE détectée'
                    );

                -- 10% de départs très précoces (ANOMALIE)
                ELSIF RANDOM() < 0.9 THEN
                    INSERT INTO attendance (employee_id, date, check_in, check_out, status, notes)
                    VALUES (
                        emp_record.id,
                        test_date,
                        TIME '08:00:00',
                        TIME '13:00:00' + (RANDOM() * INTERVAL '1 hour'),
                        'EARLY_DEPARTURE',
                        'Départ anticipé - ANOMALIE détectée'
                    );

                -- 5% d'heures supplémentaires excessives (ANOMALIE)
                ELSIF RANDOM() < 0.95 THEN
                    INSERT INTO attendance (employee_id, date, check_in, check_out, status, notes)
                    VALUES (
                        emp_record.id,
                        test_date,
                        TIME '08:00:00',
                        TIME '22:00:00' + (RANDOM() * INTERVAL '3 hours'),
                        'OVERTIME',
                        'Heures supplémentaires excessives - ANOMALIE détectée'
                    );

                -- 5% d'absences non justifiées (ANOMALIE)
                ELSE
                    INSERT INTO attendance (employee_id, date, check_in, check_out, status, notes)
                    VALUES (
                        emp_record.id,
                        test_date,
                        NULL,
                        NULL,
                        'ABSENT',
                        'Absence non justifiée - ANOMALIE détectée'
                    );
                END IF;
            END IF;
        END LOOP;
    END LOOP;

    RAISE NOTICE 'Données de pointage avec anomalies insérées';
END $$;

-- ========================================
-- 3. ANOMALIES DE PAIE (VARIATIONS SUSPECTES)
-- ========================================

-- Table payroll (si elle existe)
CREATE TABLE IF NOT EXISTS payroll (
    id BIGSERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    gross_salary DECIMAL(12,2) NOT NULL,
    net_salary DECIMAL(12,2) NOT NULL,
    deductions DECIMAL(12,2),
    bonuses DECIMAL(12,2),
    status VARCHAR(20) DEFAULT 'PENDING',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Nettoyer anciennes données
DELETE FROM payroll WHERE period_start >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '6 months';

-- Insérer des données de paie avec anomalies
DO $$
DECLARE
    emp_record RECORD;
    emp_base_salary DECIMAL(12,2);
    month_start DATE;
    i INTEGER;
    anomaly_salary DECIMAL(12,2);
BEGIN
    FOR emp_record IN SELECT e.id, e.base_salary FROM employee e LIMIT 5 LOOP
        emp_base_salary := emp_record.base_salary;

        -- Derniers 6 mois
        FOR i IN 0..5 LOOP
            month_start := DATE_TRUNC('month', CURRENT_DATE) - (i || ' months')::INTERVAL;

            -- 80% des mois : salaire normal
            IF RANDOM() < 0.8 THEN
                INSERT INTO payroll (employee_id, period_start, period_end, gross_salary, net_salary, deductions, bonuses, status)
                VALUES (
                    emp_record.id,
                    month_start,
                    month_start + INTERVAL '1 month' - INTERVAL '1 day',
                    emp_base_salary,
                    emp_base_salary * 0.77,  -- Après déductions standard
                    emp_base_salary * 0.23,
                    0,
                    'PAID'
                );

            -- 10% : Salaire anormalement élevé (ANOMALIE)
            ELSIF RANDOM() < 0.9 THEN
                anomaly_salary := emp_base_salary * (1.5 + RANDOM() * 0.5);  -- +50% à +100%
                INSERT INTO payroll (employee_id, period_start, period_end, gross_salary, net_salary, deductions, bonuses, status, notes)
                VALUES (
                    emp_record.id,
                    month_start,
                    month_start + INTERVAL '1 month' - INTERVAL '1 day',
                    anomaly_salary,
                    anomaly_salary * 0.77,
                    anomaly_salary * 0.23,
                    anomaly_salary - emp_base_salary,
                    'PAID',
                    'ANOMALIE : Salaire ' || ROUND((anomaly_salary - emp_base_salary) / emp_base_salary * 100) || '% supérieur à la normale'
                );

            -- 10% : Salaire anormalement bas (ANOMALIE)
            ELSE
                anomaly_salary := emp_base_salary * (0.3 + RANDOM() * 0.2);  -- -70% à -50%
                INSERT INTO payroll (employee_id, period_start, period_end, gross_salary, net_salary, deductions, bonuses, status, notes)
                VALUES (
                    emp_record.id,
                    month_start,
                    month_start + INTERVAL '1 month' - INTERVAL '1 day',
                    anomaly_salary,
                    anomaly_salary * 0.77,
                    anomaly_salary * 0.23,
                    0,
                    'PAID',
                    'ANOMALIE : Salaire ' || ROUND((emp_base_salary - anomaly_salary) / emp_base_salary * 100) || '% inférieur à la normale'
                );
            END IF;
        END LOOP;
    END LOOP;

    RAISE NOTICE 'Données de paie avec anomalies insérées';
END $$;

-- ========================================
-- 4. STOCKER LES ANOMALIES DÉTECTÉES
-- ========================================

-- Table pour stocker les anomalies détectées
CREATE TABLE IF NOT EXISTS ai_anomaly_detections (
    id BIGSERIAL PRIMARY KEY,
    anomaly_type VARCHAR(50) NOT NULL,
    employee_id INTEGER,
    employee_name VARCHAR(255),
    description TEXT NOT NULL,
    severity VARCHAR(20) DEFAULT 'MEDIUM',
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved BOOLEAN DEFAULT FALSE,
    data_reference TEXT
);

-- Nettoyer anciennes anomalies
TRUNCATE TABLE ai_anomaly_detections;

-- Insérer des anomalies détectées à partir des données
INSERT INTO ai_anomaly_detections (anomaly_type, employee_id, employee_name, description, severity, detected_at, resolved)
SELECT
    'ATTENDANCE_LATE' as anomaly_type,
    a.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    'Retard excessif le ' || TO_CHAR(a.date, 'DD/MM/YYYY') || ' - Arrivée à ' || TO_CHAR(a.check_in, 'HH24:MI') as description,
    'HIGH' as severity,
    a.created_at,
    false
FROM attendance a
JOIN employee e ON a.employee_id = e.id
WHERE a.status = 'LATE' AND a.check_in > TIME '10:00:00'
LIMIT 10;

INSERT INTO ai_anomaly_detections (anomaly_type, employee_id, employee_name, description, severity, detected_at, resolved)
SELECT
    'ATTENDANCE_EARLY_DEPARTURE' as anomaly_type,
    a.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    'Départ anticipé le ' || TO_CHAR(a.date, 'DD/MM/YYYY') || ' - Départ à ' || TO_CHAR(a.check_out, 'HH24:MI') as description,
    'MEDIUM' as severity,
    a.created_at,
    false
FROM attendance a
JOIN employee e ON a.employee_id = e.id
WHERE a.status = 'EARLY_DEPARTURE' AND a.check_out < TIME '15:00:00'
LIMIT 10;

INSERT INTO ai_anomaly_detections (anomaly_type, employee_id, employee_name, description, severity, detected_at, resolved)
SELECT
    'ATTENDANCE_EXCESSIVE_OVERTIME' as anomaly_type,
    a.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    'Heures supplémentaires excessives le ' || TO_CHAR(a.date, 'DD/MM/YYYY') || ' - Départ à ' || TO_CHAR(a.check_out, 'HH24:MI') as description,
    'MEDIUM' as severity,
    a.created_at,
    false
FROM attendance a
JOIN employee e ON a.employee_id = e.id
WHERE a.status = 'OVERTIME' AND a.check_out > TIME '21:00:00'
LIMIT 10;

INSERT INTO ai_anomaly_detections (anomaly_type, employee_id, employee_name, description, severity, detected_at, resolved)
SELECT
    'ATTENDANCE_ABSENT' as anomaly_type,
    a.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    'Absence non justifiée le ' || TO_CHAR(a.date, 'DD/MM/YYYY') as description,
    'HIGH' as severity,
    a.created_at,
    false
FROM attendance a
JOIN employee e ON a.employee_id = e.id
WHERE a.status = 'ABSENT'
LIMIT 10;

INSERT INTO ai_anomaly_detections (anomaly_type, employee_id, employee_name, description, severity, detected_at, resolved)
SELECT
    'PAYROLL_EXCESSIVE' as anomaly_type,
    p.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    'Salaire anormalement élevé pour la période ' || TO_CHAR(p.period_start, 'MM/YYYY') || ' : ' || p.gross_salary || ' Ar' as description,
    'HIGH' as severity,
    p.created_at,
    false
FROM payroll p
JOIN employee e ON p.employee_id = e.id
WHERE p.gross_salary > (e.base_salary * 1.4)
LIMIT 10;

INSERT INTO ai_anomaly_detections (anomaly_type, employee_id, employee_name, description, severity, detected_at, resolved)
SELECT
    'PAYROLL_INSUFFICIENT' as anomaly_type,
    p.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    'Salaire anormalement bas pour la période ' || TO_CHAR(p.period_start, 'MM/YYYY') || ' : ' || p.gross_salary || ' Ar' as description,
    'HIGH' as severity,
    p.created_at,
    false
FROM payroll p
JOIN employee e ON p.employee_id = e.id
WHERE p.gross_salary < (e.base_salary * 0.6)
LIMIT 10;

-- ========================================
-- 5. VÉRIFICATION ET STATISTIQUES
-- ========================================

-- Statistiques des anomalies par type
SELECT
    anomaly_type as "Type d'anomalie",
    COUNT(*) as "Nombre",
    COUNT(*) FILTER (WHERE severity = 'HIGH') as "Haute sévérité",
    COUNT(*) FILTER (WHERE severity = 'MEDIUM') as "Sévérité moyenne",
    COUNT(*) FILTER (WHERE resolved = true) as "Résolues",
    COUNT(*) FILTER (WHERE resolved = false) as "Non résolues"
FROM ai_anomaly_detections
GROUP BY anomaly_type
ORDER BY COUNT(*) DESC;

-- Total des anomalies
SELECT
    COUNT(*) as "Total anomalies",
    COUNT(*) FILTER (WHERE resolved = false) as "Non résolues",
    COUNT(*) FILTER (WHERE severity = 'HIGH') as "Haute sévérité"
FROM ai_anomaly_detections;

-- Anomalies récentes (derniers 7 jours)
SELECT
    anomaly_type as "Type",
    employee_name as "Employé",
    description as "Description",
    severity as "Sévérité",
    TO_CHAR(detected_at, 'DD/MM/YYYY HH24:MI') as "Détectée le"
FROM ai_anomaly_detections
WHERE detected_at >= CURRENT_TIMESTAMP - INTERVAL '7 days'
  AND resolved = false
ORDER BY detected_at DESC
LIMIT 20;

-- Employés avec le plus d'anomalies
SELECT
    employee_name as "Employé",
    COUNT(*) as "Nombre d'anomalies",
    COUNT(*) FILTER (WHERE anomaly_type LIKE 'ATTENDANCE%') as "Présence",
    COUNT(*) FILTER (WHERE anomaly_type LIKE 'PAYROLL%') as "Paie"
FROM ai_anomaly_detections
WHERE resolved = false
GROUP BY employee_id, employee_name
ORDER BY COUNT(*) DESC
LIMIT 10;

SELECT '✅ Données de test avec anomalies créées avec succès !' as "Status";
SELECT '📊 Le dashboard IA peut maintenant détecter et afficher ces anomalies' as "Info";
SELECT '💡 Exécutez insert_test_data_turnover.sql pour les prédictions de turnover' as "Note";

