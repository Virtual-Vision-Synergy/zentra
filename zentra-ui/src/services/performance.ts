import { get, post } from './api';

export interface AutoScoringRequest {
  employeeIds: number[];
  periodId: number;
}

export interface PerformanceEvaluationDetailDto {
  comment?: string;
  weightedScore?: number;
  weightUsed?: number;
  score: number;
  criterionLabel?: string;
  criterionCode?: string;
  criterionId: number;
}

export interface PerformanceEvaluationCreateRequest {
  details: PerformanceEvaluationDetailDto[];
  comments?: string;
  evaluatorName?: string;
  evaluationDate?: string;
  status?: string;
  rating?: string;
  overallScore?: number;
  periodId: number;
  employeeId: number;
  id?: number;
}

export interface PerformanceEvaluationDto extends PerformanceEvaluationCreateRequest {}

export interface PerformanceCriterionDto {
  category: string;
  defaultWeight: number;
  description?: string;
  label: string;
  code: string;
  id?: number;
}

export interface PeriodDto {
  id: number;
  code: string;
  label: string;
  startDate: string;
  endDate: string;
  status: string;
}

export interface EmployeeDto {
  id: number;
  firstName: string;
  lastName: string;
  position?: string;
  employeeNumber?: string;
}

export interface CriterionSummary {
  criterionId: number;
  criterionCode: string;
  criterionLabel: string;
  category: string;
  averageScore: number;
  weight: number;
  evaluationCount: number;
}

export interface EvaluationSummary {
  evaluationId: number;
  evaluationDate: string;
  overallScore: number;
  rating: string;
  evaluatorName?: string;
  comments?: string;
}

export interface PerformanceReportDto {
  employeeId: number;
  employeeName: string;
  employeePosition: string;
  periodId?: number;
  periodLabel?: string;
  periodStart: string;
  periodEnd: string;
  generatedDate: string;
  averageScore: number;
  overallRating: string;
  totalEvaluations: number;
  criterionSummaries: CriterionSummary[];
  evaluations: EvaluationSummary[];
  strengths: string[];
  areasForImprovement: string[];
  generalComment: string;
}

export const PerformanceService = {
  async autoScore(request: AutoScoringRequest): Promise<PerformanceEvaluationDto[]> {
    return post<PerformanceEvaluationDto[]>(`/performance/evaluations/auto-score`, request);
  },
  async listEvaluationsByPeriod(periodId: number): Promise<PerformanceEvaluationDto[]> {
    return get<PerformanceEvaluationDto[]>(`/performance/evaluations/period/${periodId}`);
  },
  async listEvaluationsByEmployee(employeeId: number): Promise<PerformanceEvaluationDto[]> {
    return get<PerformanceEvaluationDto[]>(`/performance/evaluations/employee/${employeeId}`);
  },
  async getEvaluation(id: number): Promise<PerformanceEvaluationDto> {
    return get<PerformanceEvaluationDto>(`/performance/evaluations/${id}`);
  },
  async createEvaluation(payload: PerformanceEvaluationCreateRequest): Promise<PerformanceEvaluationDto> {
    return post<PerformanceEvaluationDto>('/performance/evaluations', payload);
  },
  async createCriterion(payload: PerformanceCriterionDto): Promise<PerformanceCriterionDto> {
    return post<PerformanceCriterionDto>('/performance/criteria', payload);
  },
  async listCriteria(): Promise<PerformanceCriterionDto[]> {
    return get<PerformanceCriterionDto[]>('/performance/criteria');
  },
  async listPeriods(): Promise<PeriodDto[]> {
    return get<PeriodDto[]>('/performance/periods');
  },
  async listEmployees(): Promise<EmployeeDto[]> {
    return get<EmployeeDto[]>('/performance/employees');
  },

  // Rapports de performance
  async generateEmployeeReport(employeeId: number, periodId: number): Promise<PerformanceReportDto> {
    return get<PerformanceReportDto>(`/performance/reports/employee/${employeeId}/period/${periodId}`);
  },
  async generateEmployeeReportByDateRange(employeeId: number, startDate: string, endDate: string): Promise<PerformanceReportDto> {
    return get<PerformanceReportDto>(`/performance/reports/employee/${employeeId}?startDate=${startDate}&endDate=${endDate}`);
  },
  async generateTeamReport(periodId: number): Promise<PerformanceReportDto[]> {
    return get<PerformanceReportDto[]>(`/performance/reports/team/period/${periodId}`);
  },
};

