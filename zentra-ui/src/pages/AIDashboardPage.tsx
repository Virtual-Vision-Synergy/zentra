import React, { useState, useEffect } from 'react';
import { turnoverAPI, anomalyAPI, dataAPI } from '../services/aiService';
import '../styles/AIPages.css';

const AIDashboardPage: React.FC = () => {
  const [highRiskEmployees, setHighRiskEmployees] = useState<any[]>([]);
  const [unresolvedAnomalies, setUnresolvedAnomalies] = useState<any[]>([]);
  const [employees, setEmployees] = useState<any[]>([]);
  const [selectedEmployeeFilter, setSelectedEmployeeFilter] = useState<string>('');
  const [isLoading, setIsLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'turnover' | 'anomalies'>('turnover');

  useEffect(() => {
    loadEmployees();
  }, []);

  const loadEmployees = async () => {
    try {
      const response = await dataAPI.getEmployees();
      setEmployees(response.data);
    } catch (error) {
      console.error('Error loading employees:', error);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setIsLoading(true);
    try {
      const [turnoverResponse, anomaliesResponse] = await Promise.all([
        turnoverAPI.getHighRiskEmployees(),
        anomalyAPI.getUnresolvedAnomalies(),
      ]);
      setHighRiskEmployees(turnoverResponse.data);
      setUnresolvedAnomalies(anomaliesResponse.data);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleResolveAnomaly = async (anomalyId: number) => {
    try {
      await anomalyAPI.resolveAnomaly(anomalyId);
      setUnresolvedAnomalies((prev) => prev.filter((a) => a.id !== anomalyId));
    } catch (error) {
      console.error('Error resolving anomaly:', error);
    }
  };

  const getRiskColor = (level: string) => {
    switch (level) {
      case 'HIGH':
        return '#e74c3c';
      case 'MEDIUM':
        return '#f39c12';
      case 'LOW':
        return '#27ae60';
      default:
        return '#95a5a6';
    }
  };

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'CRITICAL':
        return '#c0392b';
      case 'HIGH':
        return '#e74c3c';
      case 'MEDIUM':
        return '#f39c12';
      case 'LOW':
        return '#3498db';
      default:
        return '#95a5a6';
    }
  };

  if (isLoading) {
    return (
      <div className="ai-page">
        <div className="loading-spinner">Chargement des données IA...</div>
      </div>
    );
  }

  return (
    <div className="ai-page">
      <div className="page-header">
        <h1>🤖 Tableau de Bord IA</h1>
        <p>Analyse prédictive et détection d'anomalies</p>
      </div>

      <div className="stats-grid">
        <div className="stat-card risk-high">
          <div className="stat-icon">⚠️</div>
          <div className="stat-content">
            <h3>{highRiskEmployees.length}</h3>
            <p>Employés à Risque</p>
          </div>
        </div>

        <div className="stat-card anomaly">
          <div className="stat-icon">🔍</div>
          <div className="stat-content">
            <h3>{unresolvedAnomalies.length}</h3>
            <p>Anomalies Non Résolues</p>
          </div>
        </div>

        <div className="stat-card critical">
          <div className="stat-icon">🚨</div>
          <div className="stat-content">
            <h3>
              {unresolvedAnomalies.filter((a) => a.severity === 'CRITICAL').length}
            </h3>
            <p>Anomalies Critiques</p>
          </div>
        </div>

        <div className="stat-card success">
          <div className="stat-icon">✅</div>
          <div className="stat-content">
            <h3>
              {highRiskEmployees.filter((e) => e.riskLevel === 'LOW').length}
            </h3>
            <p>Faible Risque</p>
          </div>
        </div>
      </div>

      <div className="tabs">
        <button
          className={`tab ${activeTab === 'turnover' ? 'active' : ''}`}
          onClick={() => setActiveTab('turnover')}
        >
          📊 Prédiction de Turnover
        </button>
        <button
          className={`tab ${activeTab === 'anomalies' ? 'active' : ''}`}
          onClick={() => setActiveTab('anomalies')}
        >
          🔍 Détection d'Anomalies
        </button>
      </div>

      {activeTab === 'turnover' && (
        <div className="data-section">
          <div className="section-header">
            <h2>Employés à Risque de Départ</h2>
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <select
                value={selectedEmployeeFilter}
                onChange={(e) => setSelectedEmployeeFilter(e.target.value)}
                className="form-control"
                style={{ width: '250px', padding: '8px' }}
              >
                <option value="">Tous les employés</option>
                {employees.map((emp) => (
                  <option key={emp.id} value={emp.id}>
                    {emp.firstName} {emp.lastName}
                  </option>
                ))}
              </select>
              <button onClick={loadData} className="btn-refresh">
                🔄 Actualiser
              </button>
            </div>
          </div>

          {(selectedEmployeeFilter
            ? highRiskEmployees.filter(e => e.employeeId === parseInt(selectedEmployeeFilter))
            : highRiskEmployees
          ).length === 0 ? (
            <div className="empty-state">
              <p>✅ Aucun employé à risque élevé détecté</p>
            </div>
          ) : (
            <div className="table-container">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Employé</th>
                    <th>Score de Risque</th>
                    <th>Niveau</th>
                    <th>Raisons</th>
                    <th>Date</th>
                  </tr>
                </thead>
                <tbody>
                  {(selectedEmployeeFilter
                    ? highRiskEmployees.filter(e => e.employeeId === parseInt(selectedEmployeeFilter))
                    : highRiskEmployees
                  ).map((employee) => (
                    <tr key={employee.employeeId}>
                      <td>
                        <strong>{employee.employeeName}</strong>
                        <br />
                        <span className="text-muted">ID: {employee.employeeId}</span>
                      </td>
                      <td>
                        <div className="risk-score">
                          <div
                            className="risk-bar"
                            style={{
                              width: `${employee.riskScore * 100}%`,
                              backgroundColor: getRiskColor(employee.riskLevel),
                            }}
                          ></div>
                          <span>{(employee.riskScore * 100).toFixed(0)}%</span>
                        </div>
                      </td>
                      <td>
                        <span
                          className="badge"
                          style={{ backgroundColor: getRiskColor(employee.riskLevel) }}
                        >
                          {employee.riskLevel}
                        </span>
                      </td>
                      <td className="reasons-cell">{employee.reasons}</td>
                      <td>{new Date(employee.predictedAt).toLocaleDateString('fr-FR')}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {activeTab === 'anomalies' && (
        <div className="data-section">
          <div className="section-header">
            <h2>Anomalies Détectées</h2>
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <select
                value={selectedEmployeeFilter}
                onChange={(e) => setSelectedEmployeeFilter(e.target.value)}
                className="form-control"
                style={{ width: '250px', padding: '8px' }}
              >
                <option value="">Tous les employés</option>
                {employees.map((emp) => (
                  <option key={emp.id} value={emp.id}>
                    {emp.firstName} {emp.lastName}
                  </option>
                ))}
              </select>
              <button onClick={loadData} className="btn-refresh">
                🔄 Actualiser
              </button>
            </div>
          </div>

          {(selectedEmployeeFilter
            ? unresolvedAnomalies.filter(a => a.employeeId === parseInt(selectedEmployeeFilter))
            : unresolvedAnomalies
          ).length === 0 ? (
            <div className="empty-state">
              <p>✅ Aucune anomalie non résolue</p>
            </div>
          ) : (
            <div className="anomalies-grid">
              {(selectedEmployeeFilter
                ? unresolvedAnomalies.filter(a => a.employeeId === parseInt(selectedEmployeeFilter))
                : unresolvedAnomalies
              ).map((anomaly) => (
                <div key={anomaly.id} className="anomaly-card">
                  <div className="anomaly-header">
                    <span
                      className="anomaly-type"
                      style={{ backgroundColor: getSeverityColor(anomaly.severity) }}
                    >
                      {anomaly.anomalyType}
                    </span>
                    <span
                      className="severity-badge"
                      style={{ color: getSeverityColor(anomaly.severity) }}
                    >
                      {anomaly.severity}
                    </span>
                  </div>

                  <div className="anomaly-content">
                    <h4>{anomaly.employeeName}</h4>
                    <p className="anomaly-description">{anomaly.description}</p>
                    <p className="anomaly-date">
                      Détectée le {new Date(anomaly.detectedAt).toLocaleString('fr-FR')}
                    </p>
                  </div>

                  <button
                    onClick={() => handleResolveAnomaly(anomaly.id)}
                    className="btn-resolve"
                  >
                    ✓ Marquer comme résolu
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default AIDashboardPage;

