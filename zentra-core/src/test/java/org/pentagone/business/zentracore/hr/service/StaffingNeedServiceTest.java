package org.pentagone.business.zentracore.hr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.StaffingNeedDto;
import org.pentagone.business.zentracore.hr.entity.Department;
import org.pentagone.business.zentracore.hr.entity.Job;
import org.pentagone.business.zentracore.hr.entity.StaffingNeed;
import org.pentagone.business.zentracore.hr.mapper.StaffingNeedMapper;
import org.pentagone.business.zentracore.hr.repository.DepartmentRepository;
import org.pentagone.business.zentracore.hr.repository.EmployeeRepository;
import org.pentagone.business.zentracore.hr.repository.JobRepository;
import org.pentagone.business.zentracore.hr.repository.StaffingNeedRepository;
import org.pentagone.business.zentracore.hr.service.impl.StaffingNeedServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffingNeedServiceTest {

    @Mock
    private StaffingNeedRepository staffingNeedRepository;

    @Mock
    private StaffingNeedMapper staffingNeedMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private StaffingNeedServiceImpl staffingNeedService;

    private StaffingNeedDto testDto;
    private StaffingNeed testEntity;

    @BeforeEach
    void setUp() {
        // Setup test DTO
        testDto = new StaffingNeedDto();
        testDto.setTitle("Développeur Backend");
        testDto.setDescription("Besoin urgent");
        testDto.setNumberOfPositions(2);
        testDto.setPriority("High");
        testDto.setStatus("Open");
        testDto.setRequiredStartDate(LocalDate.now().plusMonths(1));
        testDto.setBudgetAllocated(BigDecimal.valueOf(100000.0));
        testDto.setDepartmentId(1L);
        testDto.setJobId(1L);

        // Setup test entity
        testEntity = new StaffingNeed();
        testEntity.setId(1L);
        testEntity.setTitle("Développeur Backend");
        testEntity.setNumberOfPositions(2);
        testEntity.setStatus("Open");
        
        Department department = new Department();
        department.setId(1L);
        testEntity.setDepartment(department);
        
        Job job = new Job();
        job.setId(1L);
        testEntity.setJob(job);
    }

    @Test
    void createStaffingNeed_Success() {
        // Arrange
        when(staffingNeedMapper.toEntity(testDto)).thenReturn(testEntity);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(jobRepository.existsById(1L)).thenReturn(true);
        when(staffingNeedRepository.save(any(StaffingNeed.class))).thenReturn(testEntity);
        when(staffingNeedMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        StaffingNeedDto result = staffingNeedService.createStaffingNeed(testDto);

        // Assert
        assertNotNull(result);
        assertEquals("Développeur Backend", result.getTitle());
        verify(staffingNeedRepository, times(1)).save(any(StaffingNeed.class));
    }

    @Test
    void createStaffingNeed_InvalidTitle_ThrowsException() {
        // Arrange
        testDto.setTitle("");
        when(staffingNeedMapper.toEntity(testDto)).thenReturn(testEntity);
        testEntity.setTitle("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            staffingNeedService.createStaffingNeed(testDto);
        });
    }

    @Test
    void createStaffingNeed_InvalidNumberOfPositions_ThrowsException() {
        // Arrange
        testDto.setNumberOfPositions(0);
        when(staffingNeedMapper.toEntity(testDto)).thenReturn(testEntity);
        testEntity.setNumberOfPositions(0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            staffingNeedService.createStaffingNeed(testDto);
        });
    }

    @Test
    void createStaffingNeed_DepartmentNotFound_ThrowsException() {
        // Arrange
        when(staffingNeedMapper.toEntity(testDto)).thenReturn(testEntity);
        when(departmentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            staffingNeedService.createStaffingNeed(testDto);
        });
    }

    @Test
    void getStaffingNeedById_Success() {
        // Arrange
        when(staffingNeedRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(staffingNeedMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        StaffingNeedDto result = staffingNeedService.getStaffingNeedById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Développeur Backend", result.getTitle());
    }

    @Test
    void getStaffingNeedById_NotFound_ThrowsException() {
        // Arrange
        when(staffingNeedRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            staffingNeedService.getStaffingNeedById(999L);
        });
    }

    @Test
    void getAllStaffingNeeds_Success() {
        // Arrange
        List<StaffingNeed> entities = Arrays.asList(testEntity, testEntity);
        when(staffingNeedRepository.findAll()).thenReturn(entities);
        when(staffingNeedMapper.toDto(any(StaffingNeed.class))).thenReturn(testDto);

        // Act
        List<StaffingNeedDto> result = staffingNeedService.getAllStaffingNeeds();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getStaffingNeedsByStatus_Success() {
        // Arrange
        List<StaffingNeed> entities = Arrays.asList(testEntity);
        when(staffingNeedRepository.findByStatus("Open")).thenReturn(entities);
        when(staffingNeedMapper.toDto(any(StaffingNeed.class))).thenReturn(testDto);

        // Act
        List<StaffingNeedDto> result = staffingNeedService.getStaffingNeedsByStatus("Open");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateStaffingNeed_Success() {
        // Arrange
        testDto.setId(1L);
        when(staffingNeedRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(staffingNeedMapper.toEntity(testDto)).thenReturn(testEntity);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(jobRepository.existsById(1L)).thenReturn(true);
        when(staffingNeedRepository.save(any(StaffingNeed.class))).thenReturn(testEntity);
        when(staffingNeedMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        StaffingNeedDto result = staffingNeedService.updateStaffingNeed(testDto);

        // Assert
        assertNotNull(result);
        verify(staffingNeedRepository, times(1)).save(any(StaffingNeed.class));
    }

    @Test
    void deleteStaffingNeedById_Success() {
        // Arrange
        when(staffingNeedRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        // Act
        staffingNeedService.deleteStaffingNeedById(1L);

        // Assert
        verify(staffingNeedRepository, times(1)).delete(testEntity);
    }

    @Test
    void deleteStaffingNeedById_NotFound_ThrowsException() {
        // Arrange
        when(staffingNeedRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            staffingNeedService.deleteStaffingNeedById(999L);
        });
    }
}
