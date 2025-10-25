package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.StaffingNeedDto;

import java.util.List;

public interface StaffingNeedService {
    StaffingNeedDto createStaffingNeed(StaffingNeedDto staffingNeedDto);
    StaffingNeedDto updateStaffingNeed(StaffingNeedDto staffingNeedDto);
    StaffingNeedDto getStaffingNeedById(Long id);
    List<StaffingNeedDto> getAllStaffingNeeds();
    List<StaffingNeedDto> getStaffingNeedsByDepartment(Long departmentId);
    List<StaffingNeedDto> getStaffingNeedsByJob(Long jobId);
    List<StaffingNeedDto> getStaffingNeedsByStatus(String status);
    List<StaffingNeedDto> getStaffingNeedsByPriority(String priority);
    void deleteStaffingNeedById(Long id);
}
