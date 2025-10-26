import { useState } from 'react';
import type { StaffingNeed } from './types/StaffingNeed';
import { StaffingNeedList } from './components/StaffingNeedList';
import { StaffingNeedForm } from './components/StaffingNeedForm';
import { StaffingNeedDetail } from './components/StaffingNeedDetail';
import './StaffingNeedApp.css';

type View = 'list' | 'form' | 'detail';

export const StaffingNeedApp = () => {
  const [currentView, setCurrentView] = useState<View>('list');
  const [selectedNeed, setSelectedNeed] = useState<StaffingNeed | undefined>();

  const handleCreate = () => {
    setSelectedNeed(undefined);
    setCurrentView('form');
  };

  const handleEdit = (need: StaffingNeed) => {
    setSelectedNeed(need);
    setCurrentView('form');
  };

  const handleView = (need: StaffingNeed) => {
    setSelectedNeed(need);
    setCurrentView('detail');
  };

  const handleSave = () => {
    setCurrentView('list');
    setSelectedNeed(undefined);
  };

  const handleCancel = () => {
    setCurrentView('list');
    setSelectedNeed(undefined);
  };

  return (
    <div className="staffing-need-app">
      <header className="app-header">
        <h1>🏢 Gestion des Besoins en Personnel</h1>
        {currentView === 'list' && (
          <button className="btn-create" onClick={handleCreate}>
            ➕ Nouveau Besoin
          </button>
        )}
        {currentView !== 'list' && (
          <button className="btn-back" onClick={handleCancel}>
            ← Retour à la liste
          </button>
        )}
      </header>

      <main className="app-content">
        {currentView === 'list' && (
          <StaffingNeedList onEdit={handleEdit} onView={handleView} />
        )}

        {currentView === 'form' && (
          <StaffingNeedForm
            need={selectedNeed}
            onSave={handleSave}
            onCancel={handleCancel}
          />
        )}

        {currentView === 'detail' && selectedNeed && (
          <StaffingNeedDetail
            need={selectedNeed}
            onEdit={() => handleEdit(selectedNeed)}
            onClose={handleCancel}
          />
        )}
      </main>
    </div>
  );
};
