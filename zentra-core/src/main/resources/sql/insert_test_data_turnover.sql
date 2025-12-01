-- Script d'insertion de données de test pour les prédictions de turnover
-- À exécuter : psql -U postgres -d zentra -f insert_test_data_turnover.sql

-- ========================================
-- PRÉDICTIONS DE TURNOVER
-- ========================================

-- Table pour stocker les prédictions de turnover
CREATE TABLE IF NOT EXISTS ai_turnover_predictions (
    id BIGSERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    risk_score DOUBLE PRECISION NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    reasons TEXT,
    predicted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Nettoyer anciennes prédictions
TRUNCATE TABLE ai_turnover_predictions;

-- Insérer des prédictions de turnover avec différents niveaux de risque
DO $$
DECLARE
    emp_record RECORD;
    score DOUBLE PRECISION;
    risk VARCHAR(20);
    reasons_text TEXT;
BEGIN
    FOR emp_record IN
        SELECT e.id, e.first_name || ' ' || e.last_name as full_name,
               e.hire_date, e.base_salary
        FROM employee e
        LIMIT 5
    LOOP
        -- Calculer un score de risque basé sur plusieurs facteurs
        -- 20% : Risque TRÈS ÉLEVÉ (score >0.8)
        IF RANDOM() < 0.2 THEN
            score := 0.8 + (RANDOM() * 0.2);  -- 0.8-1.0
            risk := 'VERY_HIGH';
            reasons_text := 'Facteurs critiques: Retards répétés, baisse performance -35%, ' ||
                           'stagnation salariale 2 ans, pas d''évolution carrière, tensions management';

        -- 20% : Risque ÉLEVÉ (score 0.6-0.8)
        ELSIF RANDOM() < 0.4 THEN
            score := 0.6 + (RANDOM() * 0.2);  -- 0.6-0.8
            risk := 'HIGH';
            reasons_text := 'Facteurs élevés: Performance -20%, démotivation visible, ' ||
                           'salaire sous marché -12%, surcharge travail 50h+/sem, peu reconnaissance';

        -- 30% : Risque MOYEN (score 0.4-0.6)
        ELSIF RANDOM() < 0.7 THEN
            score := 0.4 + (RANDOM() * 0.2);  -- 0.4-0.6
            risk := 'MEDIUM';
            reasons_text := 'Facteurs modérés: Ancienneté >3 ans même poste, baisse engagement, ' ||
                           'salaire stagnant, routine installée, opportunités limitées';

        -- 30% : Risque FAIBLE (score <0.4)
        ELSE
            score := 0.1 + (RANDOM() * 0.3);  -- 0.1-0.4
            risk := 'LOW';
            reasons_text := 'Situation stable: Performance excellente, engagement fort, ' ||
                           'salaire compétitif, évolution carrière claire, bonne relation équipe';
        END IF;

        -- Insérer la prédiction
        INSERT INTO ai_turnover_predictions (
            employee_id,
            employee_name,
            risk_score,
            risk_level,
            reasons,
            predicted_at
        ) VALUES (
            emp_record.id,
            emp_record.full_name,
            ROUND(score::numeric, 2),
            risk,
            reasons_text,
            CURRENT_TIMESTAMP
        );
    END LOOP;

    RAISE NOTICE 'Prédictions de turnover créées';
END $$;

-- Ajouter des historiques de prédictions (évolution dans le temps)
INSERT INTO ai_turnover_predictions (employee_id, employee_name, risk_score, risk_level, reasons, predicted_at)
SELECT
    e.id,
    e.first_name || ' ' || e.last_name,
    CASE
        WHEN i = 3 THEN 0.45 + (RANDOM() * 0.05)  -- Il y a 3 mois: 0.45-0.50
        WHEN i = 2 THEN 0.55 + (RANDOM() * 0.05)  -- Il y a 2 mois: 0.55-0.60
        WHEN i = 1 THEN 0.68 + (RANDOM() * 0.05)  -- Il y a 1 mois: 0.68-0.73
    END,
    CASE
        WHEN i = 3 THEN 'MEDIUM'
        WHEN i = 2 THEN 'HIGH'
        WHEN i = 1 THEN 'HIGH'
    END,
    'Évolution du risque dans le temps - Tendance à la hausse',
    CURRENT_TIMESTAMP - (i || ' months')::INTERVAL
FROM employee e
CROSS JOIN generate_series(1, 3) as i
WHERE e.id IN (SELECT id FROM employee ORDER BY RANDOM() LIMIT 2)  -- 2 employés avec historique
LIMIT 6;

-- ========================================
-- STATISTIQUES PRÉDICTIONS DE TURNOVER
-- ========================================

-- Répartition par niveau de risque
SELECT
    risk_level as "Niveau de risque",
    COUNT(*) as "Nombre",
    ROUND(AVG(risk_score)::numeric * 100, 2) || '%' as "Score moyen"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days'
GROUP BY risk_level
ORDER BY
    CASE risk_level
        WHEN 'VERY_HIGH' THEN 1
        WHEN 'HIGH' THEN 2
        WHEN 'MEDIUM' THEN 3
        WHEN 'LOW' THEN 4
    END;

-- Top employés à risque (prédictions actuelles)
SELECT
    employee_name as "Employé",
    ROUND((risk_score * 100)::numeric, 2) || '%' as "Score de risque",
    risk_level as "Niveau"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days'
ORDER BY risk_score DESC
LIMIT 10;

-- Total prédictions
SELECT
    COUNT(*) as "Total prédictions",
    COUNT(*) FILTER (WHERE risk_level IN ('HIGH', 'VERY_HIGH')) as "Risque élevé",
    COUNT(*) FILTER (WHERE risk_level = 'MEDIUM') as "Risque moyen",
    COUNT(*) FILTER (WHERE risk_level = 'LOW') as "Risque faible"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days';

-- Employés avec historique d'évolution
SELECT
    employee_name as "Employé",
    TO_CHAR(predicted_at, 'YYYY-MM') as "Mois",
    ROUND((risk_score * 100)::numeric, 2) || '%' as "Score",
    risk_level as "Niveau"
FROM ai_turnover_predictions
WHERE employee_id IN (
    SELECT employee_id
    FROM ai_turnover_predictions
    GROUP BY employee_id
    HAVING COUNT(*) > 1
)
ORDER BY employee_name, predicted_at;

SELECT '✅ Prédictions de turnover générées avec succès !' as "Status";
SELECT '🎯 ' || COUNT(*) || ' prédictions créées pour ' || COUNT(DISTINCT employee_id) || ' employés' as "Info"
FROM ai_turnover_predictions;

