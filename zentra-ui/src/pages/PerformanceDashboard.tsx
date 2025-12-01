import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { PerformanceService } from '../services/performance';
import type { PerformanceEvaluationDto, PeriodDto, EmployeeDto } from '../services/performance';
import '../styles/Performance.css';

export default function PerformanceDashboard() {
  const [evaluations, setEvaluations] = useState<PerformanceEvaluationDto[]>([]);
  const [periods, setPeriods] = useState<PeriodDto[]>([]);
  const [employees, setEmployees] = useState<EmployeeDto[]>([]);
  const [periodId, setPeriodId] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [periodsData, employeesData] = await Promise.all([
          PerformanceService.listPeriods(),
          PerformanceService.listEmployees(),
        ]);
        setPeriods(periodsData);
        setEmployees(employeesData);
      } catch (e) {
        console.error(e);
        setError("Impossible de charger les données initiales.");
      }
    };

    fetchInitialData();
  }, []);

  useEffect(() => {
    if (periodId) {
      const fetchEvaluations = async () => {
        setLoading(true);
        setError('');
        try {
          const data = await PerformanceService.listEvaluationsByPeriod(Number(periodId));
          setEvaluations(data);
        } catch (e) {
          console.error(e);
          setError("Impossible de charger les évaluations.");
        } finally {
          setLoading(false);
        }
      };
      fetchEvaluations();
    }
  }, [periodId]);

  const getEmployeeName = (empId: number) => {
    const emp = employees.find(e => e.id === empId);
    return emp ? `${emp.firstName} ${emp.lastName}` : `#${empId}`;
  };

  const getPeriodLabel = (perId: number) => {
    const per = periods.find(p => p.id === perId);
    return per ? per.label : `#${perId}`;
  };

  return (
    <div className="admin-page">
      <div className="page-header">
        <div>
          <h1>Performance des collaborateurs</h1>
          <p className="page-subtitle">
            Suivez les évaluations et les scores de performance dans une vue claire et professionnelle.
          </p>
        </div>
        <div className="page-actions">
          <select
            className="period-input"
            value={periodId}
            onChange={(e) => setPeriodId(e.target.value)}
          >
            <option value="">Sélectionner une période</option>
            {periods.map((period) => (
              <option key={period.id} value={period.id}>
                {period.label} ({period.startDate} - {period.endDate})
              </option>
            ))}
          </select>
          <Link to="/admin/performance/reports" className="btn btn-secondary">
            📊 Rapports
          </Link>
          <Link to="/admin/performance/new" className="btn btn-primary">
            Nouvelle évaluation
          </Link>
        </div>
      </div>

      <div className="performance-grid">
        <div className="performance-card kpi-card">
          <div className="kpi-label">Score moyen global</div>
          <div className="kpi-value">
            {evaluations.length > 0
              ? (
                  evaluations.reduce((sum, e) => sum + (e.overallScore || 0), 0) /
                  evaluations.length
                ).toFixed(1)
              : '--'}
          </div>
        </div>

        <div className="performance-card kpi-card">
          <div className="kpi-label">Nombre d'évaluations</div>
          <div className="kpi-value">{evaluations.length}</div>
        </div>

        <div className="performance-card info-card">
          <h2>À propos</h2>
          <p>
            Visualisez et analysez les performances de vos collaborateurs
            et de préparer des rapports de performance par collaborateur.
          </p>
          <ul>
            <li>Suivez l'évolution des performances de vos équipes</li>
            <li>Consultez les scores moyens par période d'évaluation</li>
            <li>Générez des rapports détaillés individuels ou d'équipe</li>
          </ul>
        </div>
      </div>

      <div className="performance-table-section">
        <div className="section-header">
          <h2>Dernières évaluations</h2>
          <span className="section-subtitle">
            {periodId
              ? `Évaluations pour la période sélectionnée`
              : 'Sélectionnez une période pour voir les évaluations'}
          </span>
        </div>

        {loading && <div className="loading">Chargement des évaluations...</div>}
        {error && <div className="error-message">{error}</div>}

        <div className="card-table-wrapper">
          <table className="zentra-table">
            <thead>
              <tr>
                <th>Collaborateur</th>
                <th>Période</th>
                <th>Score global</th>
                <th>Appréciation</th>
                <th>Date</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {evaluations.length === 0 && !loading && (
                <tr>
                  <td colSpan={6} className="empty-state">
                    {periodId
                      ? 'Aucune évaluation enregistrée pour cette période.'
                      : 'Sélectionnez une période pour voir les évaluations.'}
                  </td>
                </tr>
              )}

              {evaluations.map((evaluation) => (
                <tr key={evaluation.id}>
                  <td>{getEmployeeName(evaluation.employeeId)}</td>
                  <td>{getPeriodLabel(evaluation.periodId)}</td>
                  <td>{evaluation.overallScore?.toFixed(1) ?? '--'}</td>
                  <td>
                    <span className={`badge badge-${(evaluation.rating || 'UNKNOWN').toLowerCase()}`}>
                      {evaluation.rating || 'N/A'}
                    </span>
                  </td>
                  <td>{evaluation.evaluationDate ?? '--'}</td>
                  <td className="table-actions">
                    {evaluation.id && (
                      <Link to={`/admin/performance/${evaluation.id}`} className="link-primary">
                        Voir le détail
                      </Link>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
