import { useEffect, useMemo, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { get, post, put } from '../services/api';
import '../styles/QcmForm.css';

interface JobListItem { id: number; title: string; }

interface PublicationFormDto {
  id?: number;
  title: string;
  description: string;
  publishedDate?: string;
  closingDate?: string;
  numberOfPositions: number;
  status: string;
  jobId: number | '';
}

export default function PublicationForm() {
  const [jobs, setJobs] = useState<JobListItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search] = useSearchParams();
  const navigate = useNavigate();

  const initial: PublicationFormDto = useMemo(() => ({
    id: undefined,
    title: '',
    description: '',
    publishedDate: new Date().toISOString().slice(0,10),
    closingDate: '',
    numberOfPositions: 1,
    status: 'Open',
    jobId: ''
  }), []);

  const [form, setForm] = useState<PublicationFormDto>(initial);

  useEffect(() => {
    const id = search.get('id');
    Promise.all([
      get<JobListItem[]>('/jobs').catch(() => [] as JobListItem[]),
      id ? get<PublicationFormDto>(`/publications/${id}`) : Promise.resolve(null)
    ]).then(([jobs, pub]) => {
      setJobs(jobs);
      if (pub) setForm({ ...pub, publishedDate: pub.publishedDate?.slice(0,10), closingDate: pub.closingDate?.slice(0,10) });
      setLoading(false);
    }).catch(() => {
      // On n'empêche pas l'édition si /jobs n'est pas dispo
      setLoading(false);
    });
  }, [search]);

  const save = async () => {
    try {
      if (!form.jobId) { setError('Veuillez sélectionner ou saisir un jobId existant'); return; }
      const payload = { ...form, jobId: Number(form.jobId) };
      if (form.id) await put('/publications', payload);
      else await post('/publications', payload);
      navigate('/admin/publications');
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Erreur lors de la sauvegarde');
    }
  };

  if (loading) return (
    <div className="admin-page">
      <div className="loading-container"><div className="loading-spinner"></div><p>Chargement...</p></div>
    </div>
  );

  return (
    <div className="admin-page">
      <div className="page-header">
        <div>
          <h1>{form.id ? 'Modifier la publication' : 'Créer une publication'}</h1>
          <p className="page-subtitle">Associer obligatoirement un job existant</p>
        </div>
        <button onClick={save} className="btn-primary">Enregistrer</button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      <div className="qcm-form">
        <div className="form-row">
          <div className="form-group">
            <label>Titre</label>
            <input value={form.title} onChange={e => setForm({...form, title: e.target.value})} />
          </div>
          <div className="form-group">
            <label>Job</label>
            {jobs.length > 0 ? (
              <select value={form.jobId} onChange={e => setForm({...form, jobId: e.target.value ? Number(e.target.value) : ''})}>
                <option value="">-- Sélectionner --</option>
                {jobs.map(j => <option key={j.id} value={j.id}>{j.title}</option>)}
              </select>
            ) : (
              <input type="number" min={1} placeholder="Saisir l'ID du job" value={form.jobId as number | ''} onChange={e => setForm({...form, jobId: e.target.value ? Number(e.target.value) : ''})} />
            )}
          </div>
        </div>
        <div className="form-row">
          <div className="form-group">
            <label>Description</label>
            <textarea rows={5} value={form.description} onChange={e => setForm({...form, description: e.target.value})} />
          </div>
        </div>
        <div className="form-row">
          <div className="form-group">
            <label>Date de publication</label>
            <input type="date" value={form.publishedDate} onChange={e => setForm({...form, publishedDate: e.target.value})} />
          </div>
          <div className="form-group">
            <label>Date de clôture</label>
            <input type="date" value={form.closingDate || ''} onChange={e => setForm({...form, closingDate: e.target.value})} />
          </div>
          <div className="form-group">
            <label>Nombre de postes</label>
            <input type="number" min={1} value={form.numberOfPositions} onChange={e => setForm({...form, numberOfPositions: Number(e.target.value)})} />
          </div>
          <div className="form-group">
            <label>Statut</label>
            <select value={form.status} onChange={e => setForm({...form, status: e.target.value})}>
              <option value="Open">Open</option>
              <option value="Closed">Closed</option>
              <option value="Suspended">Suspended</option>
              <option value="Filled">Filled</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  );
}
