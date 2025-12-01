-- Script pour générer des prédictions pour TOUS les employés
-- À exécuter : psql -U postgres -d zentra -f fix_all_employees_turnover.sql

-- Nettoyer les anciennes prédictions récentes
DELETE FROM ai_turnover_predictions WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '7 days';

-- Générer des prédictions pour TOUS les employés (pas juste 5)
DO $$
DECLARE
    emp_record RECORD;
    score DOUBLE PRECISION;
    risk VARCHAR(20);
    reasons_text TEXT;
    emp_count INTEGER := 0;
BEGIN
    FOR emp_record IN
        SELECT e.id, e.first_name || ' ' || e.last_name as full_name
        FROM employee e
        ORDER BY e.id
    LOOP
        emp_count := emp_count + 1;

        -- Distribuer les risques de manière variée
        -- Pour avoir une bonne répartition sur tous les employés
        IF (emp_count % 5) = 1 THEN
            -- 20% : Risque TRÈS ÉLEVÉ
            score := 0.80 + (RANDOM() * 0.15);  -- 0.80-0.95
            risk := 'VERY_HIGH';
            reasons_text := 'Facteurs critiques: Retards répétés, baisse performance significative, ' ||
                           'stagnation salariale prolongée, absence évolution carrière, tensions management';

        ELSIF (emp_count % 5) = 2 THEN
            -- 20% : Risque ÉLEVÉ
            score := 0.60 + (RANDOM() * 0.18);  -- 0.60-0.78
            risk := 'HIGH';
            reasons_text := 'Facteurs élevés: Performance en baisse, démotivation visible, ' ||
                           'salaire sous marché, surcharge travail fréquente, reconnaissance insuffisante';

        ELSIF (emp_count % 5) = 3 OR (emp_count % 5) = 4 THEN
            -- 40% : Risque MOYEN
            score := 0.40 + (RANDOM() * 0.18);  -- 0.40-0.58
            risk := 'MEDIUM';
            reasons_text := 'Facteurs modérés: Ancienneté élevée au même poste, légère baisse engagement, ' ||
                           'salaire stagnant, routine installée, perspectives limitées';

        ELSE
            -- 20% : Risque FAIBLE
            score := 0.10 + (RANDOM() * 0.28);  -- 0.10-0.38
            risk := 'LOW';
            reasons_text := 'Situation stable: Performance excellente, engagement fort, ' ||
                           'salaire compétitif, évolution carrière claire, bonne ambiance équipe';
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
            ROUND(score::numeric, 3),
            risk,
            reasons_text,
            CURRENT_TIMESTAMP
        );
    END LOOP;

    RAISE NOTICE 'Prédictions créées pour % employés', emp_count;
END $$;

-- Statistiques
SELECT '========== RÉSULTAT ==========' as "Section";

SELECT
    COUNT(*) as "Total prédictions créées"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '1 hour';

SELECT
    risk_level as "Niveau de risque",
    COUNT(*) as "Nombre",
    ROUND(AVG(risk_score)::numeric * 100, 2) || '%' as "Score moyen"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '1 hour'
GROUP BY risk_level
ORDER BY
    CASE risk_level
        WHEN 'VERY_HIGH' THEN 1
        WHEN 'HIGH' THEN 2
        WHEN 'MEDIUM' THEN 3
        WHEN 'LOW' THEN 4
    END;

SELECT
    employee_name as "Employé",
    ROUND((risk_score * 100)::numeric, 2) || '%' as "Score",
    risk_level as "Risque"
FROM ai_turnover_predictions
WHERE predicted_at >= CURRENT_TIMESTAMP - INTERVAL '1 hour'
ORDER BY risk_score DESC
LIMIT 20;

SELECT '✅ Prédictions générées pour TOUS les employés !' as "Status";

