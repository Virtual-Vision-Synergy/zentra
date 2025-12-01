import React, { useState, useEffect } from 'react';
import { recommendationAPI, dataAPI } from '../services/aiService';
import '../styles/AIPages.css';

interface Publication {
  id: number;
  title: string;
  description?: string;
}

const CandidateRecommendationPage: React.FC = () => {
  const [jobId, setJobId] = useState('');
  const [publications, setPublications] = useState<Publication[]>([]);
  const [selectedPublication, setSelectedPublication] = useState<Publication | null>(null);
  const [isLoadingPublications, setIsLoadingPublications] = useState(false);
  const [recommendations, setRecommendations] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPublications();
  }, []);

  const loadPublications = async () => {
    setIsLoadingPublications(true);
    try {
      const response = await dataAPI.getPublications();
      setPublications(response.data);
    } catch (err) {
      console.error('Erreur lors du chargement des publications:', err);
    } finally {
      setIsLoadingPublications(false);
    }
  };

  const handlePublicationChange = (pubId: string) => {
    setJobId(pubId);
    const publication = publications.find(p => p.id === parseInt(pubId));
    setSelectedPublication(publication || null);
  };

              const loadRecommendations = async () => {
                if (!jobId) {
                  setError('Veuillez entrer un ID de poste');
                  return;
                }

                setIsLoading(true);
                setError('');

                try {
                  const response = await recommendationAPI.getTopCandidates(parseInt(jobId), 20);
                  setRecommendations(response.data);
                } catch (err: any) {
                  setError(err.response?.data?.message || 'Erreur lors du chargement des recommandations');
                } finally {
                  setIsLoading(false);
                }
              };

              const getMatchColor = (score: number) => {
                if (score >= 0.8) return '#27ae60';
                if (score >= 0.6) return '#f39c12';
                if (score >= 0.4) return '#e67e22';
                return '#e74c3c';
              };

              const getMatchLabel = (score: number) => {
                if (score >= 0.8) return 'Excellent';
                if (score >= 0.6) return 'Bon';
                if (score >= 0.4) return 'Moyen';
                return 'Faible';
              };

              return (
                <div className="ai-page">
                  <div className="page-header">
                    <h1>🎯 Recommandation de Candidats</h1>
                    <p>Matching intelligent entre candidats et postes</p>
                  </div>

                  <div className="ai-content">
                    <div className="search-card">
                      <h2>Rechercher les meilleurs candidats</h2>

                      <div className="form-row">
                        <div className="form-group" style={{ flex: 3 }}>
                          <label>Sélectionner un poste/publication *</label>
                          <select
                            value={jobId}
                            onChange={(e) => handlePublicationChange(e.target.value)}
                            className="form-control"
                            disabled={isLoadingPublications}
                          >
                            <option value="">
                              {isLoadingPublications ? 'Chargement...' : '-- Sélectionner un poste --'}
                            </option>
                            {publications.map((pub) => (
                              <option key={pub.id} value={pub.id}>
                                {pub.title}
                              </option>
                            ))}
                          </select>
                          {selectedPublication && selectedPublication.description && (
                            <small style={{ color: '#7f8c8d', fontSize: '12px', marginTop: '4px', display: 'block' }}>
                              {selectedPublication.description.substring(0, 100)}...
                            </small>
                          )}
                        </div>

                        <div className="form-group flex-1">
                          <label>&nbsp;</label>
                          <button
                            onClick={loadRecommendations}
                            disabled={isLoading || !jobId}
                            className="btn-primary full-width"
                          >
                            {isLoading ? '⏳' : '🔍'} Rechercher
                          </button>
                        </div>
                      </div>

                      {error && <div className="error-message">{error}</div>}
                    </div>

                    {recommendations.length > 0 && (
                      <div className="recommendations-section">
                        <div className="section-header">
                          <h2>
                            {recommendations.length} Candidat{recommendations.length > 1 ? 's' : ''} Trouvé{recommendations.length > 1 ? 's' : ''}
                          </h2>
                          <p className="text-muted">
                            Triés par score de correspondance décroissant
                          </p>
                        </div>

                        <div className="candidates-grid">
                          {recommendations.map((rec, index) => (
                            <div key={rec.candidateId} className="candidate-card">
                              <div className="candidate-rank">#{index + 1}</div>

                              <div className="candidate-header">
                                <div className="candidate-avatar">
                                  {rec.candidateName.charAt(0).toUpperCase()}
                                </div>
                                <div className="candidate-info">
                                  <h3>{rec.candidateName}</h3>
                                  <p className="text-muted">ID: {rec.candidateId}</p>
                                </div>
                              </div>

                              <div className="match-score-section">
                                <div className="match-score-header">
                                  <span>Score de Correspondance</span>
                                  <span
                                    className="match-label"
                                    style={{ color: getMatchColor(rec.matchScore) }}
                                  >
                                    {getMatchLabel(rec.matchScore)}
                                  </span>
                                </div>
                                <div className="match-score-bar">
                                  <div
                                    className="match-score-fill"
                                    style={{
                                      width: `${rec.matchScore * 100}%`,
                                      backgroundColor: getMatchColor(rec.matchScore),
                                    }}
                                  >
                                    <span className="match-score-text">
                                      {(rec.matchScore * 100).toFixed(0)}%
                                    </span>
                                  </div>
                                </div>
                              </div>

                              <div className="match-details">
                                <h4>Détails de la correspondance</h4>
                                <pre className="match-details-text">{rec.matchDetails}</pre>
                              </div>

                              <div className="candidate-footer">
                                <small className="text-muted">
                                  Analysé le {new Date(rec.calculatedAt).toLocaleString('fr-FR')}
                                </small>
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>
                    )}

                    {!isLoading && recommendations.length === 0 && jobId && (
                      <div className="empty-state">
                        <div className="empty-icon">🔍</div>
                        <h3>Aucune recommandation trouvée</h3>
                        <p>
                          Aucun candidat n'a encore été analysé pour ce poste.
                          <br />
                          Veuillez lancer une analyse de matching d'abord.
                        </p>
                      </div>
                    )}
                  </div>
                </div>
              );
            };

            export default CandidateRecommendationPage;