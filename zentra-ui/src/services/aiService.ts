import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Chatbot API
    export const chatbotAPI = {
      sendMessage: (sessionId: string, message: string, userId?: number) =>
        axios.post(`${API_BASE_URL}/chatbot/chat`, {
          sessionId,
          message,
          userId,
        }),

      getSessionHistory: (sessionId: string) =>
        axios.get(`${API_BASE_URL}/chatbot/history/session/${sessionId}`),

      getUserHistory: (userId: number) =>
        axios.get(`${API_BASE_URL}/chatbot/history/user/${userId}`),

      addKnowledge: (knowledge: any) =>
        axios.post(`${API_BASE_URL}/chatbot/admin/knowledge`, knowledge),

      getAllKnowledge: () =>
        axios.get(`${API_BASE_URL}/chatbot/admin/knowledge`),

      deleteKnowledge: (id: number) =>
        axios.delete(`${API_BASE_URL}/chatbot/admin/knowledge/${id}`),
    };

    // Document Generator API
    export const documentAPI = {
      generateDocument: (request: any) =>
        axios.post(`${API_BASE_URL}/ai/documents/generate`, request),

      getEmployeeDocuments: (employeeId: number) =>
        axios.get(`${API_BASE_URL}/ai/documents/employee/${employeeId}`),

      getAllDocuments: () =>
        axios.get(`${API_BASE_URL}/ai/documents`),

      downloadDocument: (documentId: number) =>
        axios.get(`${API_BASE_URL}/ai/documents/download/${documentId}`, {
          responseType: 'blob',
        }),
    };

    // Turnover Prediction API
    export const turnoverAPI = {
      predictTurnover: (employeeId: number, employeeData: any) =>
        axios.post(`${API_BASE_URL}/ai/prediction/turnover/${employeeId}`, employeeData),

      getHighRiskEmployees: () =>
        axios.get(`${API_BASE_URL}/ai/prediction/turnover/high-risk`),

      getEmployeePrediction: (employeeId: number) =>
        axios.get(`${API_BASE_URL}/ai/prediction/turnover/employee/${employeeId}`),

      getAllPredictions: () =>
        axios.get(`${API_BASE_URL}/ai/prediction/turnover/all`),
    };

    // Anomaly Detection API
    export const anomalyAPI = {
      detectAttendanceAnomalies: (attendanceData: any[]) =>
        axios.post(`${API_BASE_URL}/ai/prediction/anomaly/attendance`, attendanceData),

      detectPayrollAnomalies: (payrollData: any[]) =>
        axios.post(`${API_BASE_URL}/ai/prediction/anomaly/payroll`, payrollData),

      getUnresolvedAnomalies: () =>
        axios.get(`${API_BASE_URL}/ai/prediction/anomaly/unresolved`),

      getEmployeeAnomalies: (employeeId: number) =>
        axios.get(`${API_BASE_URL}/ai/prediction/anomaly/employee/${employeeId}`),

      resolveAnomaly: (anomalyId: number) =>
        axios.put(`${API_BASE_URL}/ai/prediction/anomaly/${anomalyId}/resolve`),
    };

// Candidate Recommendation API
export const recommendationAPI = {
  calculateMatch: (candidateId: number, jobId: number, data: any) =>
    axios.post(`${API_BASE_URL}/ai/recommendation/match?candidateId=${candidateId}&jobId=${jobId}`, data),

  getTopCandidates: (jobId: number, limit: number = 10) =>
    axios.get(`${API_BASE_URL}/ai/recommendation/job/${jobId}/top-candidates?limit=${limit}`),

  getRecommendedJobs: (candidateId: number) =>
    axios.get(`${API_BASE_URL}/ai/recommendation/candidate/${candidateId}/recommended-jobs`),

  batchCalculateMatches: (jobId: number, data: any) =>
    axios.post(`${API_BASE_URL}/ai/recommendation/job/${jobId}/batch-match`, data),

  extractSkills: (cvText: string) =>
    axios.post(`${API_BASE_URL}/ai/recommendation/extract-skills`, { cvText }),
};

// Data API - Pour récupérer les données de la base
export const dataAPI = {
  getEmployees: () =>
    axios.get(`${API_BASE_URL}/hr/employees`),

  getEmployee: (id: number) =>
    axios.get(`${API_BASE_URL}/hr/employees/${id}`),

  getPublications: () =>
    axios.get(`${API_BASE_URL}/publications`),

  getPublication: (id: number) =>
    axios.get(`${API_BASE_URL}/publications/${id}`),

  getCandidates: () =>
    axios.get(`${API_BASE_URL}/candidates`),

  getCandidate: (id: number) =>
    axios.get(`${API_BASE_URL}/candidates/${id}`),
};

