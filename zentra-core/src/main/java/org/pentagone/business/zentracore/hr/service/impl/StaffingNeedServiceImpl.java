package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.StaffingNeedDto;
import org.pentagone.business.zentracore.hr.entity.StaffingNeed;
import org.pentagone.business.zentracore.hr.mapper.StaffingNeedMapper;
import org.pentagone.business.zentracore.hr.repository.DepartmentRepository;
import org.pentagone.business.zentracore.hr.repository.EmployeeRepository;
import org.pentagone.business.zentracore.hr.repository.JobRepository;
import org.pentagone.business.zentracore.hr.repository.StaffingNeedRepository;
import org.pentagone.business.zentracore.hr.service.StaffingNeedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffingNeedServiceImpl implements StaffingNeedService {
    
    private final StaffingNeedRepository staffingNeedRepository;
    private final StaffingNeedMapper staffingNeedMapper;
    private final DepartmentRepository departmentRepository;
    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;

    public StaffingNeedServiceImpl(
            StaffingNeedRepository staffingNeedRepository,
            StaffingNeedMapper staffingNeedMapper,
            DepartmentRepository departmentRepository,
            JobRepository jobRepository,
            EmployeeRepository employeeRepository) {
        this.staffingNeedRepository = staffingNeedRepository;
        this.staffingNeedMapper = staffingNeedMapper;
        this.departmentRepository = departmentRepository;
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
    }

    private void validateStaffingNeed(StaffingNeed staffingNeed) {
        if (staffingNeed.getTitle() == null || staffingNeed.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (staffingNeed.getNumberOfPositions() == null || staffingNeed.getNumberOfPositions() <= 0) {
            throw new IllegalArgumentException("Number of positions must be greater than 0");
        }
        if (staffingNeed.getStatus() == null || staffingNeed.getStatus().isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        if (staffingNeed.getDepartment() == null || staffingNeed.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Department is required");
        }
        if (staffingNeed.getJob() == null || staffingNeed.getJob().getId() == null) {
            throw new IllegalArgumentException("Job is required");
        }
        
        // Verify department exists
        if (!departmentRepository.existsById(staffingNeed.getDepartment().getId())) {
            throw new EntityNotFoundException("Department not found with id: " + staffingNeed.getDepartment().getId());
        }
        
        // Verify job exists
        if (!jobRepository.existsById(staffingNeed.getJob().getId())) {
            throw new EntityNotFoundException("Job not found with id: " + staffingNeed.getJob().getId());
        }
        
        // Verify employee exists if provided
        if (staffingNeed.getRequestedBy() != null && staffingNeed.getRequestedBy().getId() != null) {
            if (!employeeRepository.existsById(staffingNeed.getRequestedBy().getId())) {
                throw new EntityNotFoundException("Employee not found with id: " + staffingNeed.getRequestedBy().getId());
            }
        }
    }

    @Override
    public StaffingNeedDto createStaffingNeed(StaffingNeedDto staffingNeedDto) {
        StaffingNeed staffingNeed = staffingNeedMapper.toEntity(staffingNeedDto);
        validateStaffingNeed(staffingNeed);
        
        // Set default status if not provided
        if (staffingNeed.getStatus() == null || staffingNeed.getStatus().trim().isEmpty()) {
            staffingNeed.setStatus("Open");
        }
        
        StaffingNeed saved = staffingNeedRepository.save(staffingNeed);
        return staffingNeedMapper.toDto(saved);
    }

    @Override
    public StaffingNeedDto updateStaffingNeed(StaffingNeedDto staffingNeedDto) {
        if (staffingNeedDto.getId() == null) {
            throw new IllegalArgumentException("ID is required for update");
        }
        
        // Verify staffing need exists
        staffingNeedRepository.findById(staffingNeedDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Staffing need not found with id: " + staffingNeedDto.getId()));
        
        StaffingNeed staffingNeed = staffingNeedMapper.toEntity(staffingNeedDto);
        validateStaffingNeed(staffingNeed);
        
        StaffingNeed updated = staffingNeedRepository.save(staffingNeed);
        return staffingNeedMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffingNeedDto getStaffingNeedById(Long id) {
        StaffingNeed staffingNeed = staffingNeedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staffing need not found with id: " + id));
        return staffingNeedMapper.toDto(staffingNeed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffingNeedDto> getAllStaffingNeeds() {
        return staffingNeedRepository.findAll()
                .stream()
                .map(staffingNeedMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffingNeedDto> getStaffingNeedsByDepartment(Long departmentId) {
        return staffingNeedRepository.findByDepartmentId(departmentId)
                .stream()
                .map(staffingNeedMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffingNeedDto> getStaffingNeedsByJob(Long jobId) {
        return staffingNeedRepository.findByJobId(jobId)
                .stream()
                .map(staffingNeedMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffingNeedDto> getStaffingNeedsByStatus(String status) {
        return staffingNeedRepository.findByStatus(status)
                .stream()
                .map(staffingNeedMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffingNeedDto> getStaffingNeedsByPriority(String priority) {
        return staffingNeedRepository.findByPriority(priority)
                .stream()
                .map(staffingNeedMapper::toDto)
                .toList();
    }

    @Override
    public void deleteStaffingNeedById(Long id) {
        StaffingNeed staffingNeed = staffingNeedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staffing need not found with id: " + id));
        staffingNeedRepository.delete(staffingNeed);
    }
}
