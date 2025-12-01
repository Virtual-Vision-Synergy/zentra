-- Script de vérification des données de turnover
-- À exécuter : psql -U postgres -d zentra -f verify_turnover_data.sql

-- 1. Vérifier le nombre d'employés
SELECT '========== EMPLOYÉS ==========' as "Section";
SELECT COUNT(*) as "Total employés" FROM employee;
SELECT id, first_name, last_name, employee_number FROM employee ORDER BY id LIMIT 10;

-- 2. Vérifier toutes les prédictions
SELECT '========== TOUTES LES PRÉDICTIONS ==========' as "Section";
SELECT COUNT(*) as "Total prédictions" FROM ai_turnover_predictions;

SELECT
    id,
    employee_id,
    employee_name,
    ROUND((risk_score * 100)::numeric, 2) as "Score %",
    risk_level,
    TO_CHAR(predicted_at, 'YYYY-MM-DD HH24:MI') as "Date prédiction"
FROM ai_turnover_predictions
ORDER BY predicted_at DESC, risk_score DESC;

-- 3. Prédictions récentes uniquement (derniers 7 jours)
SELECT '========== PRÉDICTIONS RÉCENTES (7 jours) ==========' as "Section";
SELECT COUNT(*) as "Total prédictions récentes"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days';

SELECT
    employee_name,
    ROUND((risk_score * 100)::numeric, 2) as "Score %",
    risk_level,
    TO_CHAR(predicted_at, 'YYYY-MM-DD HH24:MI') as "Date"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days'
ORDER BY risk_score DESC;

-- 4. Répartition par niveau de risque
SELECT '========== RÉPARTITION PAR RISQUE ==========' as "Section";
SELECT
    risk_level,
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

-- 5. Employés sans prédiction
SELECT '========== EMPLOYÉS SANS PRÉDICTION ==========' as "Section";
SELECT
    e.id,
    e.first_name || ' ' || e.last_name as "Employé sans prédiction"
FROM employee e
WHERE NOT EXISTS (
    SELECT 1 FROM ai_turnover_predictions tp
    WHERE tp.employee_id = e.id
)
LIMIT 10;

-- 6. Vérifier les doublons (un employé ne devrait avoir qu'une prédiction récente)
SELECT '========== DOUBLONS ==========' as "Section";
SELECT
    employee_id,
    employee_name,
    COUNT(*) as "Nombre de prédictions"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days'
GROUP BY employee_id, employee_name
HAVING COUNT(*) > 1;

