package org.pentagone.business.zentracore.hr.controller;

import org.pentagone.business.zentracore.hr.dto.EmployeeDto;
import org.pentagone.business.zentracore.hr.dto.EmployeePerformanceDtos;
import org.pentagone.business.zentracore.hr.dto.PerformanceCriterionDto;
import org.pentagone.business.zentracore.hr.dto.PerformanceEvaluationPeriodDto;
import org.pentagone.business.zentracore.hr.dto.PerformanceReportDto;
import org.pentagone.business.zentracore.hr.service.PerformanceServices;
import org.pentagone.business.zentracore.hr.service.EmployeeService;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceServices.PerformanceCriterionService performanceCriterionService;
    private final PerformanceServices.PerformanceEvaluationPeriodService performanceEvaluationPeriodService;
    private final PerformanceServices.PerformanceEvaluationService performanceEvaluationService;
    private final PerformanceServices.PerformanceScoringService performanceScoringService;
    private final PerformanceServices.PerformanceReportService performanceReportService;
    private final EmployeeService employeeService;

    public PerformanceController(PerformanceServices.PerformanceCriterionService performanceCriterionService,
                                 PerformanceServices.PerformanceEvaluationPeriodService performanceEvaluationPeriodService,
                                 PerformanceServices.PerformanceEvaluationService performanceEvaluationService,
                                 PerformanceServices.PerformanceScoringService performanceScoringService,
                                 PerformanceServices.PerformanceReportService performanceReportService,
                                 EmployeeService employeeService) {
        this.performanceCriterionService = performanceCriterionService;
        this.performanceEvaluationPeriodService = performanceEvaluationPeriodService;
        this.performanceEvaluationService = performanceEvaluationService;
        this.performanceScoringService = performanceScoringService;
        this.performanceReportService = performanceReportService;
        this.employeeService = employeeService;
    }

    // ---------- Critères ----------

    @PostMapping("/criteria")
    public ResponseEntity<PerformanceCriterionDto> createCriterion(@RequestBody PerformanceCriterionDto dto) {
        PerformanceCriterionDto created = performanceCriterionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/criteria")
    public ResponseEntity<List<PerformanceCriterionDto>> getAllCriteria() {
        return ResponseEntity.ok(performanceCriterionService.findAll());
    }

    // ---------- Évaluations ----------

    @PostMapping("/evaluations")
    public ResponseEntity<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> createEvaluation(
            @RequestBody EmployeePerformanceDtos.PerformanceEvaluationCreateRequest request) {
        EmployeePerformanceDtos.EmployeePerformanceEvaluationDto created = performanceEvaluationService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/evaluations/{id}")
    public ResponseEntity<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> getEvaluationById(@PathVariable Long id) {
        return ResponseEntity.ok(performanceEvaluationService.getById(id));
    }

    @GetMapping("/evaluations/employee/{employeeId}")
    public ResponseEntity<List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto>> getEvaluationsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceEvaluationService.getByEmployee(employeeId));
    }

    @GetMapping("/evaluations/period/{periodId}")
    public ResponseEntity<List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto>> getEvaluationsByPeriod(@PathVariable Long periodId) {
        return ResponseEntity.ok(performanceEvaluationService.getByPeriod(periodId));
    }

    // ---------- Périodes ----------

    @GetMapping("/periods")
    public ResponseEntity<List<PerformanceEvaluationPeriodDto>> getAllPeriods() {
        return ResponseEntity.ok(performanceEvaluationPeriodService.findAllPeriods());
    }

    @GetMapping("/periods/{id}")
    public ResponseEntity<PerformanceEvaluationPeriodDto> getPeriodById(@PathVariable Long id) {
        return ResponseEntity.ok(performanceEvaluationPeriodService.findPeriodById(id));
    }

    // ---------- Employés (pour dropdowns) ----------

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // ---------- Scoring automatique ----------

    @PostMapping("/evaluations/auto-score")
    public ResponseEntity<List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto>> autoScore(
            @RequestBody EmployeePerformanceDtos.AutoScoringRequest request) {
        List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> list = performanceScoringService.autoScoreForEmployees(request);
        return ResponseEntity.ok(list);
    }

    // ---------- Rapports de performance ----------

    @GetMapping("/reports/employee/{employeeId}/period/{periodId}")
    public ResponseEntity<PerformanceReportDto> generateEmployeeReport(
            @PathVariable Long employeeId,
            @PathVariable Long periodId) {
        PerformanceReportDto report = performanceReportService.generateEmployeeReport(employeeId, periodId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/employee/{employeeId}")
    public ResponseEntity<PerformanceReportDto> generateEmployeeReportByDateRange(
            @PathVariable Long employeeId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        PerformanceReportDto report = performanceReportService.generateEmployeeReportByDateRange(employeeId, start, end);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/team/period/{periodId}")
    public ResponseEntity<List<PerformanceReportDto>> generateTeamReport(@PathVariable Long periodId) {
        List<PerformanceReportDto> reports = performanceReportService.generateTeamReport(periodId);
        return ResponseEntity.ok(reports);
    }
}
