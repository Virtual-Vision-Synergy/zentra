package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.EmployeePerformanceDtos;
import org.pentagone.business.zentracore.hr.dto.PerformanceCriterionDto;
import org.pentagone.business.zentracore.hr.dto.PerformanceEvaluationPeriodDto;
import org.pentagone.business.zentracore.hr.dto.PerformanceReportDto;

import java.util.List;

public interface PerformanceServices {

    interface PerformanceCriterionService {
        PerformanceCriterionDto create(PerformanceCriterionDto dto);
        PerformanceCriterionDto update(PerformanceCriterionDto dto);
        List<PerformanceCriterionDto> findAll();
        PerformanceCriterionDto findById(Long id);
        void deleteById(Long id);
    }

    interface PerformanceEvaluationPeriodService {
        List<PerformanceEvaluationPeriodDto> findAllPeriods();
        PerformanceEvaluationPeriodDto findPeriodById(Long id);
    }

    interface PerformanceEvaluationService {
        EmployeePerformanceDtos.EmployeePerformanceEvaluationDto create(EmployeePerformanceDtos.PerformanceEvaluationCreateRequest request);
        EmployeePerformanceDtos.EmployeePerformanceEvaluationDto getById(Long id);
        List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> getByEmployee(Long employeeId);
        List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> getByPeriod(Long periodId);
    }

    interface PerformanceScoringService {
        EmployeePerformanceDtos.EmployeePerformanceEvaluationDto autoScoreSingleEmployee(Long employeeId, Long periodId);
        List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> autoScoreForEmployees(EmployeePerformanceDtos.AutoScoringRequest request);
    }

    interface PerformanceReportService {
        PerformanceReportDto generateEmployeeReport(Long employeeId, Long periodId);
        PerformanceReportDto generateEmployeeReportByDateRange(Long employeeId, java.time.LocalDate startDate, java.time.LocalDate endDate);
        List<PerformanceReportDto> generateTeamReport(Long periodId);
    }
}
