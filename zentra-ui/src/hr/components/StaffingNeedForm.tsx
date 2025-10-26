import { useState, useEffect } from 'react';
import type { StaffingNeed } from '../types/StaffingNeed';
import { staffingNeedService } from '../services/staffingNeedService';
import './StaffingNeedForm.css';

interface StaffingNeedFormProps {
  need?: StaffingNeed;
  onSave: () => void;
  onCancel: () => void;
}

export const StaffingNeedForm = ({ need, onSave, onCancel }: StaffingNeedFormProps) => {
  const [formData, setFormData] = useState<StaffingNeed>({
    title: '',
    description: '',
    numberOfPositions: 1,
    priority: 'Medium',
    status: 'Open',
    requiredStartDate: '',
    budgetAllocated: 0,
    justification: '',
    departmentId: 0,
    jobId: 0,
    requestedById: undefined,
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (need) {
      setFormData(need);
    }
  }, [need]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (need?.id) {
        await staffingNeedService.updateStaffingNeed(need.id, formData);
      } else {
        await staffingNeedService.createStaffingNeed(formData);
      }
      onSave();
    } catch (err) {
      setError('Erreur lors de l\'enregistrement');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value,
    }));
  };

  return (
    <div className="staffing-need-form-container">
      <div className="form-header">
        <h2>{need ? 'Modifier le besoin' : 'Nouveau besoin en personnel'}</h2>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      <form onSubmit={handleSubmit} className="staffing-need-form">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="title">
              Titre <span className="required">*</span>
            </label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="Ex: Développeur Backend Senior"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={formData.description || ''}
              onChange={handleChange}
              rows={4}
              placeholder="Décrivez le besoin en détail..."
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="numberOfPositions">
              Nombre de postes <span className="required">*</span>
            </label>
            <input
              type="number"
              id="numberOfPositions"
              name="numberOfPositions"
              value={formData.numberOfPositions}
              onChange={handleChange}
              required
              min="1"
            />
          </div>

          <div className="form-group">
            <label htmlFor="priority">Priorité</label>
            <select
              id="priority"
              name="priority"
              value={formData.priority || 'Medium'}
              onChange={handleChange}
            >
              <option value="High">Haute</option>
              <option value="Medium">Moyenne</option>
              <option value="Low">Basse</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="status">
              Statut <span className="required">*</span>
            </label>
            <select
              id="status"
              name="status"
              value={formData.status}
              onChange={handleChange}
              required
            >
              <option value="Open">Ouvert</option>
              <option value="In Progress">En cours</option>
              <option value="Fulfilled">Satisfait</option>
              <option value="Cancelled">Annulé</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="departmentId">
              Département ID <span className="required">*</span>
            </label>
            <input
              type="number"
              id="departmentId"
              name="departmentId"
              value={formData.departmentId}
              onChange={handleChange}
              required
              min="1"
            />
          </div>

          <div className="form-group">
            <label htmlFor="jobId">
              Poste ID <span className="required">*</span>
            </label>
            <input
              type="number"
              id="jobId"
              name="jobId"
              value={formData.jobId}
              onChange={handleChange}
              required
              min="1"
            />
          </div>

          <div className="form-group">
            <label htmlFor="requestedById">Demandé par (ID Employé)</label>
            <input
              type="number"
              id="requestedById"
              name="requestedById"
              value={formData.requestedById || ''}
              onChange={handleChange}
              min="1"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="requiredStartDate">Date de début souhaitée</label>
            <input
              type="date"
              id="requiredStartDate"
              name="requiredStartDate"
              value={formData.requiredStartDate || ''}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label htmlFor="budgetAllocated">Budget alloué (€)</label>
            <input
              type="number"
              id="budgetAllocated"
              name="budgetAllocated"
              value={formData.budgetAllocated || ''}
              onChange={handleChange}
              min="0"
              step="0.01"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="justification">Justification</label>
            <textarea
              id="justification"
              name="justification"
              value={formData.justification || ''}
              onChange={handleChange}
              rows={3}
              placeholder="Justifiez ce besoin en personnel..."
            />
          </div>
        </div>

        <div className="form-actions">
          <button type="button" className="btn-cancel" onClick={onCancel} disabled={loading}>
            Annuler
          </button>
          <button type="submit" className="btn-save" disabled={loading}>
            {loading ? 'Enregistrement...' : 'Enregistrer'}
          </button>
        </div>
      </form>
    </div>
  );
};
