import { useEffect, useState } from 'react';
        import type { FormEvent } from 'react';
        import { useNavigate } from 'react-router-dom';
        import { PerformanceService } from '../services/performance';
        import type {
          PerformanceCriterionDto,
          PerformanceEvaluationCreateRequest,
          PerformanceEvaluationDetailDto,
          EmployeeDto,
          PeriodDto,
        } from '../services/performance';
        import '../styles/Performance.css';

        export default function PerformanceEvaluationForm() {
          const navigate = useNavigate();
          const [criteria, setCriteria] = useState<PerformanceCriterionDto[]>([]);
          const [employeeId, setEmployeeId] = useState('');
          const [periodId, setPeriodId] = useState('');
          const [employees, setEmployees] = useState<EmployeeDto[]>([]);
          const [periods, setPeriods] = useState<PeriodDto[]>([]);
          const [scores, setScores] = useState<Record<number, number>>({});
          const [evaluatorName, setEvaluatorName] = useState('');
          const [comments, setComments] = useState('');
          const [loading, setLoading] = useState(false);
          const [error, setError] = useState<string | null>(null);
          const [success, setSuccess] = useState<string | null>(null);

          useEffect(() => {
            const fetchData = async () => {
              try {
                setLoading(true);
                const [criteriaData, employeesData, periodsData] = await Promise.all([
                  PerformanceService.listCriteria(),
                  PerformanceService.listEmployees(),
                  PerformanceService.listPeriods(),
                ]);
                setCriteria(criteriaData);
                setEmployees(employeesData);
                setPeriods(periodsData);
              } catch (e) {
                console.error(e);
                setError("Impossible de charger les données nécessaires.");
              } finally {
                setLoading(false);
              }
            };

            fetchData();
          }, []);

          const handleScoreChange = (criterionId: number, value: string) => {
            const num = parseFloat(value);
            setScores((prev) => ({ ...prev, [criterionId]: isNaN(num) ? 0 : num }));
          };

          const handleSubmit = async (e: FormEvent) => {
            e.preventDefault();
            setError(null);
            setSuccess(null);

            if (!employeeId || !periodId) {
              setError('Veuillez renseigner le collaborateur et la période.');
              return;
            }

            const details: PerformanceEvaluationDetailDto[] = criteria.map((criterion) => ({
              criterionId: criterion.id!,
              score: scores[criterion.id!] ?? 0,
            }));

            const payload: PerformanceEvaluationCreateRequest = {
              employeeId: Number(employeeId),
              periodId: Number(periodId),
              evaluatorName: evaluatorName || undefined,
              comments: comments || undefined,
              details,
            };

            try {
              setLoading(true);
              await PerformanceService.createEvaluation(payload);
              setSuccess('Évaluation créée avec succès.');
              setTimeout(() => navigate('/admin/performance'), 1200);
            } catch (e) {
              console.error(e);
              setError("Une erreur est survenue lors de la création de l'évaluation.");
            } finally {
              setLoading(false);
            }
          };

          return (
            <div className="admin-page">
              <div className="page-header">
                <div>
                  <h1>Nouvelle évaluation de performance</h1>
                  <p className="page-subtitle">
                    Enregistrez une évaluation structurée avec des critères pondérés pour un collaborateur.
                  </p>
                </div>
              </div>

              <div className="form-card">
                {error && <div className="error-message">{error}</div>}
                {success && <div className="success-message">{success}</div>}

                <form onSubmit={handleSubmit} className="performance-form">
                  <div className="form-grid">
                    <div className="form-group">
                      <label>Collaborateur</label>
                      <select
                        value={employeeId}
                        onChange={(e) => setEmployeeId(e.target.value)}
                        required
                      >
                        <option value="">Sélectionner un collaborateur</option>
                        {employees.map((emp) => (
                          <option key={emp.id} value={emp.id}>
                            {emp.firstName} {emp.lastName} {emp.position ? `- ${emp.position}` : ''}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="form-group">
                      <label>Période</label>
                      <select
                        value={periodId}
                        onChange={(e) => setPeriodId(e.target.value)}
                        required
                      >
                        <option value="">Sélectionner une période</option>
                        {periods.map((period) => (
                          <option key={period.id} value={period.id}>
                            {period.label} ({period.startDate} - {period.endDate})
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="form-group">
                      <label>Nom de l'évaluateur (optionnel)</label>
                      <input
                        type="text"
                        value={evaluatorName}
                        onChange={(e) => setEvaluatorName(e.target.value)}
                        placeholder="Ex : Jean Dupont"
                      />
                    </div>

                    <div className="form-group">
                      <label>Commentaires (optionnel)</label>
                      <textarea
                        value={comments}
                        onChange={(e) => setComments(e.target.value)}
                        placeholder="Commentaires généraux sur l'évaluation"
                        rows={4}
                      />
                    </div>
                  </div>

                  <div className="criteria-section">
                    <h2>Critères de performance</h2>
                    <p className="section-subtitle">
                      Saisissez une note pour chaque critère (par exemple sur 100). Les poids seront appliqués côté backend.
                    </p>

                    {loading && <div className="loading">Chargement des critères...</div>}

                    <div className="criteria-grid">
                      {criteria.map((criterion) => (
                        <div key={criterion.id} className="criteria-card">
                          <div className="criteria-header">
                            <h3>{criterion.label}</h3>
                            <span className="criteria-category">{criterion.category}</span>
                          </div>
                          {criterion.description && <p className="criteria-description">{criterion.description}</p>}
                          <div className="criteria-input-row">
                            <div className="criteria-input-group">
                              <label>Note</label>
                              <input
                                type="number"
                                min={0}
                                max={100}
                                value={scores[criterion.id!] ?? ''}
                                onChange={(e) => handleScoreChange(criterion.id!, e.target.value)}
                                placeholder="0 - 100"
                              />
                            </div>
                            <div className="criteria-weight">
                              <span>Poids par défaut :</span>
                              <strong>{criterion.defaultWeight}</strong>
                            </div>
                          </div>
                        </div>
                      ))}

                      {criteria.length === 0 && !loading && (
                        <div className="empty-state">
                          Aucun critère configuré pour le moment. Créez d'abord des critères côté backend pour utiliser ce formulaire.
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="form-actions">
                    <button type="button" className="btn btn-secondary" onClick={() => navigate(-1)}>
                      Annuler
                    </button>
                    <button type="submit" className="btn btn-primary" disabled={loading}>
                      {loading ? 'Enregistrement...' : 'Enregistrer l\'évaluation'}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          );
        }