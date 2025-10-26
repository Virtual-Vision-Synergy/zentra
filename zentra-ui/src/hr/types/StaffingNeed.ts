export interface StaffingNeed {
  id?: number;
  title: string;
  description?: string;
  numberOfPositions: number;
  priority?: 'High' | 'Medium' | 'Low';
  status: 'Open' | 'In Progress' | 'Fulfilled' | 'Cancelled';
  requiredStartDate?: string;
  budgetAllocated?: number;
  justification?: string;
  departmentId: number;
  departmentName?: string;
  jobId: number;
  jobTitle?: string;
  requestedById?: number;
  requestedByName?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Department {
  id: number;
  name: string;
  description?: string;
  annualBudget?: number;
}

export interface Job {
  id: number;
  title: string;
  description?: string;
  departmentId: number;
}

export interface Employee {
  id: number;
  firstName: string;
  lastName: string;
  workEmail: string;
}
