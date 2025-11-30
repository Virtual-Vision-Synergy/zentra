import React, { useState, useEffect } from 'react';
import { documentAPI, dataAPI } from '../services/aiService';
import '../styles/AIPages.css';

interface Employee {
  id: number;
  firstName: string;
  lastName: string;
  position?: string;
  salary?: number;
}

interface DocumentRequest {
  documentType: string;
  employeeId: number;
  additionalData: {
    employeeName?: string;
    contractType?: string;
    startDate?: string;
    endDate?: string;
    salary?: number;
    position?: string;
    grossSalary?: number;
    period?: string;
  };
}

const DocumentGeneratorPage: React.FC = () => {
  const [documentType, setDocumentType] = useState('CONTRACT');
  const [employeeId, setEmployeeId] = useState('');
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selectedEmployee, setSelectedEmployee] = useState<Employee | null>(null);
  const [isLoadingEmployees, setIsLoadingEmployees] = useState(false);
  const [contractType, setContractType] = useState('CDI');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [salary, setSalary] = useState('');
  const [position, setPosition] = useState('');
  const [period, setPeriod] = useState('');
  const [isGenerating, setIsGenerating] = useState(false);
  const [generatedDocument, setGeneratedDocument] = useState<any>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    loadEmployees();
  }, []);

  const loadEmployees = async () => {
    setIsLoadingEmployees(true);
    try {
      const response = await dataAPI.getEmployees();
      setEmployees(response.data);
    } catch (err) {
      console.error('Erreur lors du chargement des employés:', err);
      setError('Impossible de charger la liste des employés');
    } finally {
      setIsLoadingEmployees(false);
    }
  };

  const handleEmployeeChange = (empId: string) => {
    setEmployeeId(empId);
    const employee = employees.find(e => e.id === parseInt(empId));
    setSelectedEmployee(employee || null);

    // Pré-remplir les champs si l'employé a des données
    if (employee) {
      if (employee.position) setPosition(employee.position);
      if (employee.salary) setSalary(employee.salary.toString());
    }
  };

  const handleGenerate = async () => {
    if (!employeeId || !selectedEmployee) {
      setError('Veuillez sélectionner un employé');
      return;
    }

    setIsGenerating(true);
    setError('');

    const employeeName = `${selectedEmployee.firstName} ${selectedEmployee.lastName}`;

    const request: DocumentRequest = {
      documentType,
      employeeId: parseInt(employeeId),
      additionalData: {
        employeeName,
      },
    };

    if (documentType === 'CONTRACT') {
      request.additionalData.contractType = contractType;
      request.additionalData.startDate = startDate;
      request.additionalData.salary = parseFloat(salary);
      request.additionalData.position = position;
    } else if (documentType === 'ATTESTATION' || documentType === 'CERTIFICATE') {
      request.additionalData.startDate = startDate;
      request.additionalData.position = position;
      if (endDate) request.additionalData.endDate = endDate;
    } else if (documentType === 'PAYSLIP') {
      request.additionalData.grossSalary = parseFloat(salary);
      request.additionalData.period = period;
    }

    try {
      const response = await documentAPI.generateDocument(request);
      setGeneratedDocument(response.data);
      setError('');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Erreur lors de la génération du document');
    } finally {
      setIsGenerating(false);
    }
  };

  const resetForm = () => {
    setEmployeeId('');
    setSelectedEmployee(null);
    setContractType('CDI');
    setStartDate('');
    setEndDate('');
    setSalary('');
    setPosition('');
    setPeriod('');
    setGeneratedDocument(null);
    setError('');
  };

  return (
    <div className="ai-page">
      <div className="page-header">
        <h1>📄 Génération Automatique de Documents RH</h1>
        <p>Créez automatiquement des contrats, attestations et autres documents</p>
      </div>

      <div className="ai-content">
        <div className="form-card">
          <h2>Informations du document</h2>

          <div className="form-group">
            <label>Type de document *</label>
            <select
              value={documentType}
              onChange={(e) => setDocumentType(e.target.value)}
              className="form-control"
            >
              <option value="CONTRACT">Contrat de travail</option>
              <option value="ATTESTATION">Attestation de travail</option>
              <option value="CERTIFICATE">Certificat de travail</option>
              <option value="PAYSLIP">Bulletin de paie</option>
            </select>
          </div>

          <div className="form-group">
            <label>Sélectionner un employé *</label>
            <select
              value={employeeId}
              onChange={(e) => handleEmployeeChange(e.target.value)}
              className="form-control"
              disabled={isLoadingEmployees}
            >
              <option value="">
                {isLoadingEmployees ? 'Chargement...' : '-- Sélectionner un employé --'}
              </option>
              {employees.map((emp) => (
                <option key={emp.id} value={emp.id}>
                  {emp.firstName} {emp.lastName} {emp.position ? `- ${emp.position}` : ''}
                </option>
              ))}
            </select>
            {selectedEmployee && (
              <small style={{ color: '#7f8c8d', fontSize: '12px', marginTop: '4px', display: 'block' }}>
                ID: {selectedEmployee.id} | Poste: {selectedEmployee.position || 'Non défini'}
              </small>
            )}
          </div>

          {documentType === 'CONTRACT' && (
            <>
              <div className="form-row">
                <div className="form-group">
                  <label>Type de contrat</label>
                  <select
                    value={contractType}
                    onChange={(e) => setContractType(e.target.value)}
                    className="form-control"
                  >
                    <option value="CDI">CDI</option>
                    <option value="CDD">CDD</option>
                    <option value="Stage">Stage</option>
                    <option value="Alternance">Alternance</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Date de début</label>
                  <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    className="form-control"
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Poste</label>
                  <input
                    type="text"
                    value={position}
                    onChange={(e) => setPosition(e.target.value)}
                    className="form-control"
                    placeholder="Développeur"
                  />
                </div>

                <div className="form-group">
                  <label>Salaire brut mensuel (€)</label>
                  <input
                    type="number"
                    value={salary}
                    onChange={(e) => setSalary(e.target.value)}
                    className="form-control"
                    placeholder="2500"
                  />
                </div>
              </div>
            </>
          )}

          {(documentType === 'ATTESTATION' || documentType === 'CERTIFICATE') && (
            <>
              <div className="form-row">
                <div className="form-group">
                  <label>Poste</label>
                  <input
                    type="text"
                    value={position}
                    onChange={(e) => setPosition(e.target.value)}
                    className="form-control"
                    placeholder="Développeur"
                  />
                </div>

                <div className="form-group">
                  <label>Date de début</label>
                  <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    className="form-control"
                  />
                </div>
              </div>

              {documentType === 'CERTIFICATE' && (
                <div className="form-group">
                  <label>Date de fin</label>
                  <input
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    className="form-control"
                  />
                </div>
              )}
            </>
          )}

          {documentType === 'PAYSLIP' && (
            <div className="form-row">
              <div className="form-group">
                <label>Salaire brut (€)</label>
                <input
                  type="number"
                  value={salary}
                  onChange={(e) => setSalary(e.target.value)}
                  className="form-control"
                  placeholder="2500"
                />
              </div>

              <div className="form-group">
                <label>Période</label>
                <input
                  type="month"
                  value={period}
                  onChange={(e) => setPeriod(e.target.value)}
                  className="form-control"
                />
              </div>
            </div>
          )}

          {error && <div className="error-message">{error}</div>}

          <div className="form-actions">
            <button
              onClick={handleGenerate}
              disabled={isGenerating}
              className="btn-primary"
            >
              {isGenerating ? '⏳ Génération...' : '✨ Générer le document'}
            </button>
            <button onClick={resetForm} className="btn-secondary">
              🔄 Réinitialiser
            </button>
          </div>
        </div>

        {generatedDocument && (
          <div className="result-card success">
            <h3>✅ Document généré avec succès !</h3>
            <div className="document-info">
              <p><strong>Nom du fichier :</strong> {generatedDocument.fileName}</p>
              <p><strong>Type :</strong> {generatedDocument.documentType}</p>
              <p><strong>Employé :</strong> {generatedDocument.employeeName}</p>
              <p><strong>Généré le :</strong> {new Date(generatedDocument.generatedAt).toLocaleString('fr-FR')}</p>
            </div>
            <p className="info-text">
              Le document a été généré et enregistré. Il est disponible dans le dossier des documents générés.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default DocumentGeneratorPage;

