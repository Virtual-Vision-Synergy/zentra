import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { PerformanceService } from '../services/performance';
import type { PerformanceReportDto, PeriodDto, EmployeeDto } from '../services/performance';
import '../styles/Performance.css';

export default function PerformanceReport() {
  const [searchParams] = useSearchParams();
  const [report, setReport] = useState<PerformanceReportDto | null>(null);
  const [teamReports, setTeamReports] = useState<PerformanceReportDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [periods, setPeriods] = useState<PeriodDto[]>([]);
  const [employees, setEmployees] = useState<EmployeeDto[]>([]);

  const [selectedEmployee, setSelectedEmployee] = useState<string>('');
  const [selectedPeriod, setSelectedPeriod] = useState<string>('');
  const [reportType, setReportType] = useState<'individual' | 'team'>('individual');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [periodsData, employeesData] = await Promise.all([
          PerformanceService.listPeriods(),
          PerformanceService.listEmployees(),
        ]);
        setPeriods(periodsData);
        setEmployees(employeesData);

        // Charger depuis les paramètres URL si présents
        const empId = searchParams.get('employeeId');
        const perId = searchParams.get('periodId');
        if (empId) setSelectedEmployee(empId);
        if (perId) setSelectedPeriod(perId);

        if (empId && perId) {
          await handleGenerateReport(Number(empId), Number(perId));
        }
      } catch (e) {
        console.error(e);
        setError('Impossible de charger les données.');
      }
    };
    fetchData();
  }, []);

  const handleGenerateReport = async (empId?: number, perId?: number) => {
    const employeeId = empId || Number(selectedEmployee);
    const periodId = perId || Number(selectedPeriod);

    if (!employeeId || !periodId) {
      setError('Veuillez sélectionner un collaborateur et une période.');
      return;
    }

    setLoading(true);
    setError('');
    setReport(null);
    setTeamReports([]);

    try {
      if (reportType === 'individual') {
        const data = await PerformanceService.generateEmployeeReport(employeeId, periodId);
        setReport(data);
      } else {
        const data = await PerformanceService.generateTeamReport(periodId);
        setTeamReports(data);
      }
    } catch (e: any) {
      console.error(e);
      setError(e.message || 'Erreur lors de la génération du rapport.');
    } finally {
      setLoading(false);
    }
  };

  const getRatingBadgeClass = (rating: string) => {
    switch (rating) {
      case 'EXCELLENT': return 'badge-excellent';
      case 'GOOD': return 'badge-good';
      case 'AVERAGE': return 'badge-average';
      case 'POOR': return 'badge-poor';
      default: return 'badge-unknown';
    }
  };

  const getRatingLabel = (rating: string) => {
    switch (rating) {
      case 'EXCELLENT': return 'Excellent';
      case 'GOOD': return 'Bon';
      case 'AVERAGE': return 'Moyen';
      case 'POOR': return 'Faible';
      default: return 'N/A';
    }
  };

  const handlePrint = () => {
    window.print();
  };

  const handleExportPDF = async () => {
    try {
      // Dynamically import jsPDF and html2canvas
      const jsPDF = (await import('jspdf')).default;
      const html2canvas = (await import('html2canvas')).default;

      const reportElement = document.querySelector('.performance-report') as HTMLElement;
      if (!reportElement) {
        alert('Aucun rapport à exporter');
        return;
      }

      // Hide buttons before capture
      const buttons = reportElement.querySelectorAll('.no-print');
      buttons.forEach((btn) => (btn as HTMLElement).style.display = 'none');

      // Capture the report as canvas
      const canvas = await html2canvas(reportElement, {
        scale: 2,
        useCORS: true,
        logging: false,
        backgroundColor: '#ffffff',
      });

      // Show buttons again
      buttons.forEach((btn) => (btn as HTMLElement).style.display = '');

      const imgData = canvas.toDataURL('image/png');
      const pdf = new jsPDF({
        orientation: 'portrait',
        unit: 'mm',
        format: 'a4',
      });

      const imgWidth = 210; // A4 width in mm
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      let heightLeft = imgHeight;
      let position = 0;

      // Add first page
      pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
      heightLeft -= 297; // A4 height in mm

      // Add additional pages if needed
      while (heightLeft > 0) {
        position = heightLeft - imgHeight;
        pdf.addPage();
        pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
        heightLeft -= 297;
      }

      // Generate filename
      const fileName = report
        ? `Rapport_Performance_${report.employeeName.replace(/\s+/g, '_')}_${new Date().toISOString().split('T')[0]}.pdf`
        : `Rapport_Equipe_${new Date().toISOString().split('T')[0]}.pdf`;

      pdf.save(fileName);
    } catch (error) {
      console.error('Erreur lors de l\'export PDF:', error);
      alert('Erreur lors de l\'exportation PDF. Veuillez réessayer.');
    }
  };

  return (
    <div className="admin-page">
      <div className="page-header">
        <div>
          <h1>Rapports de Performance</h1>
          <p className="page-subtitle">
            Générez des rapports détaillés de performance pour vos collaborateurs.
          </p>
        </div>
      </div>

      {/* Filtres */}
      <div className="performance-card">
        <h3>Paramètres du rapport</h3>
        <div className="filter-row" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginTop: '1rem' }}>
          <div>
            <label className="form-label">Type de rapport</label>
            <select
              className="form-control"
              value={reportType}
              onChange={(e) => setReportType(e.target.value as 'individual' | 'team')}
            >
              <option value="individual">Individuel</option>
              <option value="team">Équipe</option>
            </select>
          </div>

          {reportType === 'individual' && (
            <div>
              <label className="form-label">Collaborateur</label>
              <select
                className="form-control"
                value={selectedEmployee}
                onChange={(e) => setSelectedEmployee(e.target.value)}
              >
                <option value="">Sélectionner un collaborateur</option>
                {employees.map((emp) => (
                  <option key={emp.id} value={emp.id}>
                    {emp.firstName} {emp.lastName} {emp.position ? `- ${emp.position}` : ''}
                  </option>
                ))}
              </select>
            </div>
          )}

          <div>
            <label className="form-label">Période</label>
            <select
              className="form-control"
              value={selectedPeriod}
              onChange={(e) => setSelectedPeriod(e.target.value)}
            >
              <option value="">Sélectionner une période</option>
              {periods.map((period) => (
                <option key={period.id} value={period.id}>
                  {period.label} ({period.startDate} - {period.endDate})
                </option>
              ))}
            </select>
          </div>

          <div style={{ display: 'flex', alignItems: 'flex-end' }}>
            <button
              className="btn btn-primary"
              onClick={() => handleGenerateReport()}
              disabled={loading}
            >
              {loading ? 'Génération...' : 'Générer le rapport'}
            </button>
          </div>
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      {/* Rapport individuel */}
      {report && (
        <div className="performance-report">
          <div className="report-header">
            <div className="report-title-section">
              <h2>Rapport de Performance - {report.employeeName}</h2>
              <p className="report-meta">
                Poste: {report.employeePosition} | Période: {report.periodLabel || `${report.periodStart} - ${report.periodEnd}`} |
                Généré le: {new Date(report.generatedDate).toLocaleDateString('fr-FR')}
              </p>
            </div>
            <div className="report-actions no-print">
              <button className="btn btn-secondary" onClick={handlePrint}>
                📄 Imprimer
              </button>
              <button className="btn btn-secondary" onClick={handleExportPDF}>
                📥 Exporter PDF
              </button>
            </div>
          </div>

          {/* Vue d'ensemble */}
          <div className="report-section">
            <h3>📊 Vue d'ensemble</h3>
            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-label">Score moyen</div>
                <div className="stat-value">{report.averageScore.toFixed(1)}%</div>
              </div>
              <div className="stat-card">
                <div className="stat-label">Appréciation globale</div>
                <div className="stat-value">
                  <span className={`badge ${getRatingBadgeClass(report.overallRating)}`}>
                    {getRatingLabel(report.overallRating)}
                  </span>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-label">Nombre d'évaluations</div>
                <div className="stat-value">{report.totalEvaluations}</div>
              </div>
            </div>
          </div>

          {/* Commentaire général */}
          {report.generalComment && (
            <div className="report-section">
              <h3>💬 Synthèse</h3>
              <p className="general-comment">{report.generalComment}</p>
            </div>
          )}

          {/* Performance par critère */}
          {report.criterionSummaries && report.criterionSummaries.length > 0 && (
            <div className="report-section">
              <h3>📈 Performance par critère</h3>
              <table className="zentra-table">
                <thead>
                  <tr>
                    <th>Critère</th>
                    <th>Catégorie</th>
                    <th>Score moyen</th>
                    <th>Poids</th>
                    <th>Nb évaluations</th>
                  </tr>
                </thead>
                <tbody>
                  {report.criterionSummaries.map((criterion) => (
                    <tr key={criterion.criterionId}>
                      <td>
                        <strong>{criterion.criterionLabel}</strong>
                        <br />
                        <small className="text-muted">{criterion.criterionCode}</small>
                      </td>
                      <td>{criterion.category}</td>
                      <td>
                        <strong>{criterion.averageScore.toFixed(1)}%</strong>
                        <div className="progress-bar-mini">
                          <div
                            className="progress-fill"
                            style={{ width: `${criterion.averageScore}%` }}
                          ></div>
                        </div>
                      </td>
                      <td>{criterion.weight.toFixed(2)}</td>
                      <td>{criterion.evaluationCount}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Forces et axes d'amélioration */}
          <div className="report-section">
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
              <div>
                <h3>💪 Points forts</h3>
                <ul className="recommendation-list">
                  {report.strengths.map((strength, idx) => (
                    <li key={idx} className="strength-item">{strength}</li>
                  ))}
                </ul>
              </div>
              <div>
                <h3>🎯 Axes d'amélioration</h3>
                <ul className="recommendation-list">
                  {report.areasForImprovement.map((area, idx) => (
                    <li key={idx} className="improvement-item">{area}</li>
                  ))}
                </ul>
              </div>
            </div>
          </div>

          {/* Historique des évaluations */}
          {report.evaluations && report.evaluations.length > 0 && (
            <div className="report-section">
              <h3>📅 Historique des évaluations</h3>
              <table className="zentra-table">
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Score</th>
                    <th>Appréciation</th>
                    <th>Évaluateur</th>
                    <th>Commentaires</th>
                  </tr>
                </thead>
                <tbody>
                  {report.evaluations.map((evaluation) => (
                    <tr key={evaluation.evaluationId}>
                      <td>{new Date(evaluation.evaluationDate).toLocaleDateString('fr-FR')}</td>
                      <td><strong>{evaluation.overallScore.toFixed(1)}%</strong></td>
                      <td>
                        <span className={`badge ${getRatingBadgeClass(evaluation.rating)}`}>
                          {getRatingLabel(evaluation.rating)}
                        </span>
                      </td>
                      <td>{evaluation.evaluatorName || 'N/A'}</td>
                      <td className="text-truncate">{evaluation.comments || '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* Rapport d'équipe */}
      {teamReports.length > 0 && (
        <div className="performance-report">
          <div className="report-header">
            <h2>Rapport d'Équipe</h2>
            <div className="report-actions no-print">
              <button className="btn btn-secondary" onClick={handlePrint}>
                📄 Imprimer
              </button>
              <button className="btn btn-secondary" onClick={handleExportPDF}>
                📥 Exporter PDF
              </button>
            </div>
          </div>

          <div className="report-section">
            <table className="zentra-table">
              <thead>
                <tr>
                  <th>Collaborateur</th>
                  <th>Poste</th>
                  <th>Score moyen</th>
                  <th>Appréciation</th>
                  <th>Nb évaluations</th>
                  <th className="no-print">Actions</th>
                </tr>
              </thead>
              <tbody>
                {teamReports.map((rep) => (
                  <tr key={rep.employeeId}>
                    <td><strong>{rep.employeeName}</strong></td>
                    <td>{rep.employeePosition}</td>
                    <td><strong>{rep.averageScore.toFixed(1)}%</strong></td>
                    <td>
                      <span className={`badge ${getRatingBadgeClass(rep.overallRating)}`}>
                        {getRatingLabel(rep.overallRating)}
                      </span>
                    </td>
                    <td>{rep.totalEvaluations}</td>
                    <td className="no-print">
                      <button
                        className="btn-link"
                        onClick={() => {
                          setReportType('individual');
                          setSelectedEmployee(String(rep.employeeId));
                          handleGenerateReport(rep.employeeId, rep.periodId);
                        }}
                      >
                        Voir détails
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

