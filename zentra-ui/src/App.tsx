import { BrowserRouter, Routes, Route } from 'react-router-dom';
import UserLogin from './pages/UserLogin';
import QcmAttempt from './pages/QcmAttempt';
import Success from './pages/Success';
import AdminLayout from './components/AdminLayout';
import Dashboard from './pages/Dashboard';
import QcmList from './pages/QcmList';
import QcmDetails from './pages/QcmDetails';
import QcmForm from './pages/QcmForm';
import InterviewList from './pages/InterviewList';
import InterviewDetails from './pages/InterviewDetails';
import InterviewForm from './pages/InterviewForm';
import { StaffingNeedApp } from './hr/StaffingNeedApp';
import PublicationsPage from './pages/PublicationsPage';
import PublicationForm from './pages/PublicationForm';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Routes candidats */}
        <Route path="/" element={<UserLogin />} />
        <Route path="/qcm-attempt" element={<QcmAttempt />} />
        <Route path="/success" element={<Success />} />
        <Route path="/apply/:publicationId" element={<CandidateApply />} />

        {/* Routes admin */}
        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="qcms" element={<QcmList />} />
          <Route path="qcms/:id" element={<QcmDetails />} />
          <Route path="qcms/:id/edit" element={<QcmForm />} />

          {/* Publications */}
          <Route path="publications" element={<PublicationsPage />} />
          <Route path="publications/new" element={<PublicationForm />} />
          <Route path="interviews" element={<InterviewList />} />
          <Route path="interviews/:id" element={<InterviewDetails />} />
          <Route path="interviews/:id/edit" element={<InterviewForm />} />
          <Route path="besoins" element={<StaffingNeedApp />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
