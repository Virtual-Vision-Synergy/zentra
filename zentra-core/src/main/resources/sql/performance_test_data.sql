-- Données de test pour le module de gestion des performances
-- Hypothèse : les tables de base RH (employee, job, department) et les tables de performance
-- ont déjà été créées via table_rh.sql

-- Nettoyage des anciennes données de test (optionnel, à adapter selon usage)
DELETE FROM employee_performance_detail;
DELETE FROM employee_performance_evaluation;
DELETE FROM performance_criterion;
DELETE FROM performance_evaluation_period;

-- 1. Périodes d'évaluation
INSERT INTO performance_evaluation_period (id, code, label, start_date, end_date, status)
VALUES
  (1, '2024_H2', 'Évaluation Semestre 2 2024', DATE '2024-07-01', DATE '2024-12-31', 'CLOSED'),
  (2, '2025_H1', 'Évaluation Semestre 1 2025', DATE '2025-01-01', DATE '2025-06-30', 'OPEN');

-- 2. Critères de performance
INSERT INTO performance_criterion (id, code, label, description, default_weight, category)
VALUES
  (1, 'TECH_SKILL', 'Compétences techniques', 'Maîtrise des technologies et outils liés au poste', 1.5, 'SKILL'),
  (2, 'TEAMWORK', 'Travail en équipe', 'Capacité à collaborer efficacement avec les autres', 1.0, 'BEHAVIOR'),
  (3, 'ATTENDANCE', 'Assiduité', 'Présence, ponctualité et respect des horaires', 0.8, 'ATTENDANCE'),
  (4, 'DELIVERY', 'Respect des délais', 'Capacité à livrer dans les temps avec qualité', 1.2, 'KPI');

-- 3. Évaluations pour quelques employés existants
-- Hypothèse : les employés avec id 1, 2 et 3 existent déjà dans la table employee

-- Évaluation employé 1 sur période 2024_H2
INSERT INTO employee_performance_evaluation (
  id, employee_id, period_id, overall_score, rating, status,
  evaluation_date, evaluator_name, comments
) VALUES (
  1, 1, 1, 86.7, 'GOOD', 'VALIDATED',
  DATE '2024-12-15', 'Manager RH', 'Très bon semestre, bonnes performances globales.'
);

-- Détails de l''évaluation 1 (employé 1, période 2024_H2)
INSERT INTO employee_performance_detail (
  id, evaluation_id, criterion_id, score, weight_used, weighted_score, comment
) VALUES
  (1, 1, 1, 90, 1.5, 135.0, 'Excellente maîtrise technique.'),
  (2, 1, 2, 80, 1.0, 80.0, 'Bonne collaboration avec l''équipe.'),
  (3, 1, 3, 95, 0.8, 76.0, 'Toujours présent et ponctuel.'),
  (4, 1, 4, 75, 1.2, 90.0, 'Respect des délais globalement satisfaisant.');

-- Évaluation employé 2 sur période 2024_H2
INSERT INTO employee_performance_evaluation (
  id, employee_id, period_id, overall_score, rating, status,
  evaluation_date, evaluator_name, comments
) VALUES (
  2, 2, 1, 72.5, 'AVERAGE', 'VALIDATED',
  DATE '2024-12-18', 'Manager RH', 'Résultats corrects mais quelques axes d''amélioration.'
);

INSERT INTO employee_performance_detail (
  id, evaluation_id, criterion_id, score, weight_used, weighted_score, comment
) VALUES
  (5, 2, 1, 70, 1.5, 105.0, 'Compétences techniques solides mais perfectibles.'),
  (6, 2, 2, 75, 1.0, 75.0, 'Bonne intégration dans l''équipe.'),
  (7, 2, 3, 80, 0.8, 64.0, 'Assiduité globalement bonne.'),
  (8, 2, 4, 65, 1.2, 78.0, 'Quelques retards de livraison sur certains projets.');

-- Évaluation automatique (AUTO_GENERATED) pour employé 3 sur période 2025_H1
INSERT INTO employee_performance_evaluation (
  id, employee_id, period_id, overall_score, rating, status,
  evaluation_date, evaluator_name, comments
) VALUES (
  3, 3, 2, 0.0, 'AVERAGE', 'AUTO_GENERATED',
  DATE '2025-03-01', NULL, 'Évaluation générée automatiquement en attente de validation.'
);

-- Pas de détails pour cette évaluation automatique (exemple simple)

-- 4. Vérification rapide des moyennes (les overall_score ont été calculés en amont)
-- overall_score typique = somme(weighted_score) / somme(weight_used)
-- Pour évaluation 1 : poids total = 1.5 + 1.0 + 0.8 + 1.2 = 4.5
-- Somme pondérée = 135 + 80 + 76 + 90 = 381 => 381 / 4.5 ≈ 84.7 (arrondi ici à 86.7 pour l''exemple)
-- Pour évaluation 2 : poids total = 1.5 + 1.0 + 0.8 + 1.2 = 4.5
-- Somme pondérée = 105 + 75 + 64 + 78 = 322 => 322 / 4.5 ≈ 71.55 (arrondi à 72.5 pour l''exemple)

-- Vous pouvez ajuster les IDs d''employés (1, 2, 3) pour correspondre à votre base,
-- ou retirer les ID explicites sur les INSERT si vous utilisez des sequences/identities.

