import type { StaffingNeed } from '../types/StaffingNeed';

const API_BASE_URL = 'http://localhost:8080/api';

export const staffingNeedService = {
  // Obtenir tous les besoins
  async getAllStaffingNeeds(): Promise<StaffingNeed[]> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs`);
    if (!response.ok) throw new Error('Failed to fetch staffing needs');
    return response.json();
  },

  // Obtenir un besoin par ID
  async getStaffingNeedById(id: number): Promise<StaffingNeed> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs/${id}`);
    if (!response.ok) throw new Error('Failed to fetch staffing need');
    return response.json();
  },

  // Créer un nouveau besoin
  async createStaffingNeed(staffingNeed: StaffingNeed): Promise<StaffingNeed> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(staffingNeed),
    });
    if (!response.ok) throw new Error('Failed to create staffing need');
    return response.json();
  },

  // Mettre à jour un besoin
  async updateStaffingNeed(id: number, staffingNeed: StaffingNeed): Promise<StaffingNeed> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(staffingNeed),
    });
    if (!response.ok) throw new Error('Failed to update staffing need');
    return response.json();
  },

  // Supprimer un besoin
  async deleteStaffingNeed(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete staffing need');
  },

  // Filtrer par statut
  async getByStatus(status: string): Promise<StaffingNeed[]> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs/status/${status}`);
    if (!response.ok) throw new Error('Failed to fetch staffing needs by status');
    return response.json();
  },

  // Filtrer par priorité
  async getByPriority(priority: string): Promise<StaffingNeed[]> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs/priority/${priority}`);
    if (!response.ok) throw new Error('Failed to fetch staffing needs by priority');
    return response.json();
  },

  // Filtrer par département
  async getByDepartment(departmentId: number): Promise<StaffingNeed[]> {
    const response = await fetch(`${API_BASE_URL}/staffing-needs/department/${departmentId}`);
    if (!response.ok) throw new Error('Failed to fetch staffing needs by department');
    return response.json();
  },
};
