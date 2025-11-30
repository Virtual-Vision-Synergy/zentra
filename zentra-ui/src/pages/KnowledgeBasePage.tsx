import React, { useState, useEffect } from 'react';
import { chatbotAPI } from '../services/aiService';
import '../styles/AIPages.css';

interface Knowledge {
  id?: number;
  category: string;
  question: string;
  answer: string;
  keywords: string;
  active: boolean;
}

const KnowledgeBasePage: React.FC = () => {
  const [knowledgeList, setKnowledgeList] = useState<Knowledge[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Knowledge>({
    category: 'GENERAL',
    question: '',
    answer: '',
    keywords: '',
    active: true,
  });
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    loadKnowledge();
  }, []);

  const loadKnowledge = async () => {
    setIsLoading(true);
    try {
      const response = await chatbotAPI.getAllKnowledge();
      setKnowledgeList(response.data);
    } catch (err) {
      setError('Erreur lors du chargement des connaissances');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    if (!formData.question || !formData.answer) {
      setError('La question et la réponse sont obligatoires');
      return;
    }

    try {
      await chatbotAPI.addKnowledge(formData);
      setSuccessMessage('Connaissance ajoutée avec succès !');
      setShowForm(false);
      setFormData({
        category: 'GENERAL',
        question: '',
        answer: '',
        keywords: '',
        active: true,
      });
      loadKnowledge();
    } catch (err) {
      setError('Erreur lors de l\'ajout de la connaissance');
      console.error(err);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Êtes-vous sûr de vouloir supprimer cette connaissance ?')) {
      return;
    }

    try {
      await chatbotAPI.deleteKnowledge(id);
      setSuccessMessage('Connaissance supprimée avec succès !');
      loadKnowledge();
    } catch (err) {
      setError('Erreur lors de la suppression');
      console.error(err);
    }
  };

  const getCategoryBadgeColor = (category: string) => {
    switch (category) {
      case 'LEAVE': return '#3498db';
      case 'PAYROLL': return '#27ae60';
      case 'ATTENDANCE': return '#f39c12';
      case 'CONTRACT': return '#9b59b6';
      default: return '#95a5a6';
    }
  };

  const getCategoryLabel = (category: string) => {
    switch (category) {
      case 'LEAVE': return 'Congés';
      case 'PAYROLL': return 'Paie';
      case 'ATTENDANCE': return 'Présence';
      case 'CONTRACT': return 'Contrat';
      default: return 'Général';
    }
  };

  if (isLoading) {
    return (
      <div className="ai-page">
        <div className="loading-spinner">Chargement de la base de connaissances...</div>
      </div>
    );
  }

  return (
    <div className="ai-page">
      <div className="page-header">
        <h1>📚 Base de Connaissances du Chatbot</h1>
        <p>Gérez les questions et réponses automatiques du chatbot RH</p>
      </div>

      <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))' }}>
        <div className="stat-card">
          <div className="stat-icon">📝</div>
          <div className="stat-content">
            <h3>{knowledgeList.length}</h3>
            <p>Total Connaissances</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-content">
            <h3>{knowledgeList.filter(k => k.active).length}</h3>
            <p>Actives</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">💼</div>
          <div className="stat-content">
            <h3>{knowledgeList.filter(k => k.category === 'LEAVE').length}</h3>
            <p>Congés</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">💰</div>
          <div className="stat-content">
            <h3>{knowledgeList.filter(k => k.category === 'PAYROLL').length}</h3>
            <p>Paie</p>
          </div>
        </div>
      </div>

      {error && <div className="error-message" style={{ marginBottom: '20px' }}>{error}</div>}
      {successMessage && (
        <div className="result-card success" style={{ marginBottom: '20px' }}>
          <h3>✅ {successMessage}</h3>
        </div>
      )}

      <div className="ai-content">
        <div className="section-header">
          <h2>Connaissances ({knowledgeList.length})</h2>
          <button
            onClick={() => {
              setShowForm(!showForm);
              setEditingId(null);
              setFormData({
                category: 'GENERAL',
                question: '',
                answer: '',
                keywords: '',
                active: true,
              });
            }}
            className="btn-primary"
          >
            {showForm ? '❌ Annuler' : '➕ Ajouter une connaissance'}
          </button>
        </div>

        {showForm && (
          <div className="form-card" style={{ marginBottom: '24px' }}>
            <h2>{editingId ? 'Modifier' : 'Nouvelle'} Connaissance</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Catégorie *</label>
                <select
                  value={formData.category}
                  onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                  className="form-control"
                >
                  <option value="GENERAL">Général</option>
                  <option value="LEAVE">Congés</option>
                  <option value="PAYROLL">Paie</option>
                  <option value="ATTENDANCE">Présence</option>
                  <option value="CONTRACT">Contrat</option>
                </select>
              </div>

              <div className="form-group">
                <label>Question *</label>
                <input
                  type="text"
                  value={formData.question}
                  onChange={(e) => setFormData({ ...formData, question: e.target.value })}
                  className="form-control"
                  placeholder="Ex: Comment demander un congé ?"
                  required
                />
              </div>

              <div className="form-group">
                <label>Réponse *</label>
                <textarea
                  value={formData.answer}
                  onChange={(e) => setFormData({ ...formData, answer: e.target.value })}
                  className="form-control"
                  rows={5}
                  placeholder="Réponse détaillée à la question..."
                  required
                  style={{ resize: 'vertical' }}
                />
              </div>

              <div className="form-group">
                <label>Mots-clés (séparés par des virgules)</label>
                <input
                  type="text"
                  value={formData.keywords}
                  onChange={(e) => setFormData({ ...formData, keywords: e.target.value })}
                  className="form-control"
                  placeholder="congé, vacances, demande, formulaire"
                />
                <small style={{ color: '#7f8c8d', fontSize: '12px' }}>
                  Ajoutez des mots-clés pour améliorer la recherche
                </small>
              </div>

              <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <input
                  type="checkbox"
                  id="active"
                  checked={formData.active}
                  onChange={(e) => setFormData({ ...formData, active: e.target.checked })}
                />
                <label htmlFor="active" style={{ margin: 0, cursor: 'pointer' }}>
                  Activer cette connaissance
                </label>
              </div>

              <div className="form-actions">
                <button type="submit" className="btn-primary">
                  ✅ {editingId ? 'Modifier' : 'Ajouter'}
                </button>
                <button
                  type="button"
                  onClick={() => setShowForm(false)}
                  className="btn-secondary"
                >
                  ❌ Annuler
                </button>
              </div>
            </form>
          </div>
        )}

        <div className="knowledge-grid" style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))',
          gap: '20px'
        }}>
          {knowledgeList.map((knowledge) => (
            <div key={knowledge.id} className="knowledge-card" style={{
              background: 'white',
              border: '1px solid #ecf0f1',
              borderRadius: '12px',
              padding: '20px',
              transition: 'all 0.2s'
            }}>
              <div className="knowledge-header" style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '16px'
              }}>
                <span
                  className="category-badge"
                  style={{
                    backgroundColor: getCategoryBadgeColor(knowledge.category),
                    color: 'white',
                    padding: '4px 12px',
                    borderRadius: '12px',
                    fontSize: '12px',
                    fontWeight: '600'
                  }}
                >
                  {getCategoryLabel(knowledge.category)}
                </span>
                <span
                  className="status-badge"
                  style={{
                    color: knowledge.active ? '#27ae60' : '#95a5a6',
                    fontSize: '12px',
                    fontWeight: '600'
                  }}
                >
                  {knowledge.active ? '● Actif' : '○ Inactif'}
                </span>
              </div>

              <div className="knowledge-content">
                <h4 style={{ color: '#2c3e50', marginBottom: '12px' }}>❓ {knowledge.question}</h4>
                <p style={{ color: '#7f8c8d', fontSize: '14px', lineHeight: '1.6', marginBottom: '12px' }}>
                  {knowledge.answer}
                </p>
                {knowledge.keywords && (
                  <div style={{ marginTop: '8px' }}>
                    <small style={{ color: '#95a5a6', fontSize: '11px' }}>
                      🏷️ Mots-clés: {knowledge.keywords}
                    </small>
                  </div>
                )}
              </div>

              <div className="knowledge-actions" style={{
                display: 'flex',
                gap: '8px',
                marginTop: '16px',
                paddingTop: '16px',
                borderTop: '1px solid #ecf0f1'
              }}>
                <button
                  onClick={() => handleDelete(knowledge.id!)}
                  className="btn-delete"
                  style={{
                    flex: 1,
                    padding: '8px',
                    background: '#e74c3c',
                    color: 'white',
                    border: 'none',
                    borderRadius: '6px',
                    cursor: 'pointer',
                    fontSize: '13px',
                    fontWeight: '500'
                  }}
                >
                  🗑️ Supprimer
                </button>
              </div>
            </div>
          ))}
        </div>

        {knowledgeList.length === 0 && (
          <div className="empty-state">
            <div className="empty-icon">📚</div>
            <h3>Aucune connaissance trouvée</h3>
            <p>Commencez par ajouter des questions et réponses pour le chatbot</p>
            <button onClick={() => setShowForm(true)} className="btn-primary" style={{ marginTop: '16px' }}>
              ➕ Ajouter la première connaissance
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default KnowledgeBasePage;

