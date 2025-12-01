package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.hr.dto.EmployeePerformanceDtos;
import org.pentagone.business.zentracore.hr.dto.PerformanceCriterionDto;
import org.pentagone.business.zentracore.hr.dto.PerformanceEvaluationPeriodDto;
import org.pentagone.business.zentracore.hr.dto.PerformanceReportDto;
import org.pentagone.business.zentracore.hr.entity.Employee;
import org.pentagone.business.zentracore.hr.entity.EmployeePerformanceDetail;
import org.pentagone.business.zentracore.hr.entity.EmployeePerformanceEvaluation;
import org.pentagone.business.zentracore.hr.entity.PerformanceCriterion;
import org.pentagone.business.zentracore.hr.entity.PerformanceEvaluationPeriod;
import org.pentagone.business.zentracore.hr.repository.EmployeePerformanceDetailRepository;
import org.pentagone.business.zentracore.hr.repository.EmployeePerformanceEvaluationRepository;
import org.pentagone.business.zentracore.hr.repository.EmployeeRepository;
import org.pentagone.business.zentracore.hr.repository.PerformanceCriterionRepository;
import org.pentagone.business.zentracore.hr.repository.PerformanceEvaluationPeriodRepository;
import org.pentagone.business.zentracore.hr.service.PerformanceServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceServicesImpl implements PerformanceServices.PerformanceCriterionService,
        PerformanceServices.PerformanceEvaluationPeriodService,
        PerformanceServices.PerformanceEvaluationService,
        PerformanceServices.PerformanceScoringService,
        PerformanceServices.PerformanceReportService {

    private final PerformanceCriterionRepository performanceCriterionRepository;
    private final PerformanceEvaluationPeriodRepository performanceEvaluationPeriodRepository;
    private final EmployeePerformanceEvaluationRepository employeePerformanceEvaluationRepository;
    private final EmployeePerformanceDetailRepository employeePerformanceDetailRepository;
    private final EmployeeRepository employeeRepository;

    public PerformanceServicesImpl(PerformanceCriterionRepository performanceCriterionRepository,
                                   PerformanceEvaluationPeriodRepository performanceEvaluationPeriodRepository,
                                   EmployeePerformanceEvaluationRepository employeePerformanceEvaluationRepository,
                                   EmployeePerformanceDetailRepository employeePerformanceDetailRepository,
                                   EmployeeRepository employeeRepository) {
        this.performanceCriterionRepository = performanceCriterionRepository;
        this.performanceEvaluationPeriodRepository = performanceEvaluationPeriodRepository;
        this.employeePerformanceEvaluationRepository = employeePerformanceEvaluationRepository;
        this.employeePerformanceDetailRepository = employeePerformanceDetailRepository;
        this.employeeRepository = employeeRepository;
    }

    // -------------------- Criterion --------------------

    @Override
    public PerformanceCriterionDto create(PerformanceCriterionDto dto) {
        PerformanceCriterion entity = new PerformanceCriterion();
        entity.setCode(dto.getCode());
        entity.setLabel(dto.getLabel());
        entity.setDescription(dto.getDescription());
        entity.setDefaultWeight(dto.getDefaultWeight());
        entity.setCategory(dto.getCategory());
        entity = performanceCriterionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public PerformanceCriterionDto update(PerformanceCriterionDto dto) {
        PerformanceCriterion entity = performanceCriterionRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Performance criterion not found"));
        entity.setLabel(dto.getLabel());
        entity.setDescription(dto.getDescription());
        entity.setDefaultWeight(dto.getDefaultWeight());
        entity.setCategory(dto.getCategory());
        performanceCriterionRepository.save(entity);
        return dto;
    }

    @Override
    public List<PerformanceCriterionDto> findAll() {
        return performanceCriterionRepository.findAll().stream().map(entity -> {
            PerformanceCriterionDto dto = new PerformanceCriterionDto();
            dto.setId(entity.getId());
            dto.setCode(entity.getCode());
            dto.setLabel(entity.getLabel());
            dto.setDescription(entity.getDescription());
            dto.setDefaultWeight(entity.getDefaultWeight());
            dto.setCategory(entity.getCategory());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public PerformanceCriterionDto findById(Long id) {
        PerformanceCriterion entity = performanceCriterionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Performance criterion not found"));
        PerformanceCriterionDto dto = new PerformanceCriterionDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setLabel(entity.getLabel());
        dto.setDescription(entity.getDescription());
        dto.setDefaultWeight(entity.getDefaultWeight());
        dto.setCategory(entity.getCategory());
        return dto;
    }

    @Override
    public void deleteById(Long id) {
        performanceCriterionRepository.deleteById(id);
    }

    // -------------------- Evaluation --------------------

    @Override
    @Transactional
    public EmployeePerformanceDtos.EmployeePerformanceEvaluationDto create(EmployeePerformanceDtos.PerformanceEvaluationCreateRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        PerformanceEvaluationPeriod period = performanceEvaluationPeriodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new IllegalArgumentException("Period not found"));

        EmployeePerformanceEvaluation evaluation = new EmployeePerformanceEvaluation();
        evaluation.setEmployee(employee);
        evaluation.setPeriod(period);
        evaluation.setEvaluationDate(LocalDate.now());
        evaluation.setEvaluatorName(request.getEvaluatorName());
        evaluation.setComments(request.getComments());
        evaluation.setStatus("DRAFT");

        List<EmployeePerformanceDetail> details = new ArrayList<>();
        double totalWeight = 0.0;
        double totalWeighted = 0.0;

        for (EmployeePerformanceDtos.EmployeePerformanceDetailDto d : request.getDetails()) {
            PerformanceCriterion criterion = performanceCriterionRepository.findById(d.getCriterionId())
                    .orElseThrow(() -> new IllegalArgumentException("Criterion not found"));
            EmployeePerformanceDetail detail = new EmployeePerformanceDetail();
            detail.setCriterion(criterion);
            detail.setScore(d.getScore());
            double weight = d.getWeightUsed() != null ? d.getWeightUsed() : criterion.getDefaultWeight();
            detail.setWeightUsed(weight);
            double weightedScore = d.getScore() * weight;
            detail.setWeightedScore(weightedScore);
            detail.setComment(d.getComment());
            detail.setEvaluation(evaluation);
            details.add(detail);
            totalWeight += weight;
            totalWeighted += weightedScore;
        }

        evaluation.setDetails(details);

        if (totalWeight > 0) {
            double overallScore = totalWeighted / totalWeight;
            evaluation.setOverallScore(overallScore);
            evaluation.setRating(computeRating(overallScore));
        }

        EmployeePerformanceEvaluation saved = employeePerformanceEvaluationRepository.save(evaluation);
        employeePerformanceDetailRepository.saveAll(details);

        return toDto(saved);
    }

    @Override
    public EmployeePerformanceDtos.EmployeePerformanceEvaluationDto getById(Long id) {
        EmployeePerformanceEvaluation evaluation = employeePerformanceEvaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found"));
        return toDto(evaluation);
    }

    @Override
    public List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> getByEmployee(Long employeeId) {
        return employeePerformanceEvaluationRepository.findByEmployeeId(employeeId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> getByPeriod(Long periodId) {
        return employeePerformanceEvaluationRepository.findByPeriodId(periodId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // -------------------- Scoring automatique --------------------

    @Override
    @Transactional
    public EmployeePerformanceDtos.EmployeePerformanceEvaluationDto autoScoreSingleEmployee(Long employeeId, Long periodId) {
        EmployeePerformanceDtos.AutoScoringRequest request = new EmployeePerformanceDtos.AutoScoringRequest();
        request.setPeriodId(periodId);
        request.setEmployeeIds(java.util.Collections.singletonList(employeeId));
        List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> list = autoScoreForEmployees(request);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional
    public List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> autoScoreForEmployees(EmployeePerformanceDtos.AutoScoringRequest request) {
        // V1 simple: pour chaque employé, créer une évaluation avec un seul critère synthétique "GLOBAL" à 0
        List<EmployeePerformanceDtos.EmployeePerformanceEvaluationDto> result = new ArrayList<>();
        if (request.getEmployeeIds() == null || request.getEmployeeIds().isEmpty()) {
            return result;
        }

        PerformanceEvaluationPeriod period = performanceEvaluationPeriodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new IllegalArgumentException("Period not found"));

        for (Long employeeId : request.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));
            EmployeePerformanceEvaluation evaluation = new EmployeePerformanceEvaluation();
            evaluation.setEmployee(employee);
            evaluation.setPeriod(period);
            evaluation.setEvaluationDate(LocalDate.now());
            evaluation.setStatus("AUTO_GENERATED");
            evaluation.setOverallScore(0.0);
            evaluation.setRating("AVERAGE");

            EmployeePerformanceEvaluation saved = employeePerformanceEvaluationRepository.save(evaluation);
            result.add(toDto(saved));
        }
        return result;
    }

    private String computeRating(double overallScore) {
        if (overallScore >= 90) return "EXCELLENT";
        if (overallScore >= 75) return "GOOD";
        if (overallScore >= 60) return "AVERAGE";
        return "POOR";
    }

    private EmployeePerformanceDtos.EmployeePerformanceEvaluationDto toDto(EmployeePerformanceEvaluation evaluation) {
        EmployeePerformanceDtos.EmployeePerformanceEvaluationDto dto = new EmployeePerformanceDtos.EmployeePerformanceEvaluationDto();
        dto.setId(evaluation.getId());
        dto.setEmployeeId(evaluation.getEmployee().getId());
        dto.setPeriodId(evaluation.getPeriod().getId());
        dto.setOverallScore(evaluation.getOverallScore());
        dto.setRating(evaluation.getRating());
        dto.setStatus(evaluation.getStatus());
        dto.setEvaluationDate(evaluation.getEvaluationDate());
        dto.setEvaluatorName(evaluation.getEvaluatorName());
        dto.setComments(evaluation.getComments());
        List<EmployeePerformanceDtos.EmployeePerformanceDetailDto> detailDtos = evaluation.getDetails().stream().map(detail -> {
            EmployeePerformanceDtos.EmployeePerformanceDetailDto d = new EmployeePerformanceDtos.EmployeePerformanceDetailDto();
            d.setCriterionId(detail.getCriterion().getId());
            d.setCriterionCode(detail.getCriterion().getCode());
            d.setCriterionLabel(detail.getCriterion().getLabel());
            d.setScore(detail.getScore());
            d.setWeightUsed(detail.getWeightUsed());
            d.setWeightedScore(detail.getWeightedScore());
            d.setComment(detail.getComment());
            return d;
        }).collect(Collectors.toList());
        dto.setDetails(detailDtos);
        return dto;
    }

    // -------------------- Period Service --------------------

    @Override
    public List<PerformanceEvaluationPeriodDto> findAllPeriods() {
        return performanceEvaluationPeriodRepository.findAll().stream().map(entity -> {
            PerformanceEvaluationPeriodDto dto = new PerformanceEvaluationPeriodDto();
            dto.setId(entity.getId());
            dto.setCode(entity.getCode());
            dto.setLabel(entity.getLabel());
            dto.setStartDate(entity.getStartDate());
            dto.setEndDate(entity.getEndDate());
            dto.setStatus(entity.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public PerformanceEvaluationPeriodDto findPeriodById(Long id) {
        PerformanceEvaluationPeriod entity = performanceEvaluationPeriodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Period not found"));
        PerformanceEvaluationPeriodDto dto = new PerformanceEvaluationPeriodDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setLabel(entity.getLabel());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    // -------------------- Performance Report Service --------------------

    @Override
    public PerformanceReportDto generateEmployeeReport(Long employeeId, Long periodId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        PerformanceEvaluationPeriod period = performanceEvaluationPeriodRepository.findById(periodId)
                .orElseThrow(() -> new IllegalArgumentException("Period not found"));

        return buildReport(employee, period, null, null);
    }

    @Override
    public PerformanceReportDto generateEmployeeReportByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        return buildReport(employee, null, startDate, endDate);
    }

    @Override
    public List<PerformanceReportDto> generateTeamReport(Long periodId) {
        PerformanceEvaluationPeriod period = performanceEvaluationPeriodRepository.findById(periodId)
                .orElseThrow(() -> new IllegalArgumentException("Period not found"));

        List<Employee> employees = employeeRepository.findAll();
        List<PerformanceReportDto> reports = new ArrayList<>();

        for (Employee employee : employees) {
            try {
                PerformanceReportDto report = buildReport(employee, period, null, null);
                if (report.getTotalEvaluations() > 0) {
                    reports.add(report);
                }
            } catch (Exception e) {
                // Skip employees with no evaluations
            }
        }

        return reports;
    }

    private PerformanceReportDto buildReport(Employee employee, PerformanceEvaluationPeriod period,
                                            LocalDate startDate, LocalDate endDate) {
        PerformanceReportDto report = new PerformanceReportDto();

        // Informations de base
        report.setEmployeeId(employee.getId());
        report.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        report.setEmployeePosition(employee.getJob() != null ? employee.getJob().getTitle() : "N/A");
        report.setGeneratedDate(LocalDate.now());

        // Informations de période
        if (period != null) {
            report.setPeriodId(period.getId());
            report.setPeriodLabel(period.getLabel());
            report.setPeriodStart(period.getStartDate());
            report.setPeriodEnd(period.getEndDate());
        } else {
            report.setPeriodStart(startDate);
            report.setPeriodEnd(endDate);
        }

        // Récupération des évaluations
        List<EmployeePerformanceEvaluation> evaluations;
        if (period != null) {
            evaluations = employeePerformanceEvaluationRepository
                    .findAllByEmployeeIdAndPeriodId(employee.getId(), period.getId());
        } else {
            evaluations = employeePerformanceEvaluationRepository.findByEmployeeId(employee.getId())
                    .stream()
                    .filter(e -> e.getEvaluationDate() != null &&
                                 !e.getEvaluationDate().isBefore(startDate) &&
                                 !e.getEvaluationDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        report.setTotalEvaluations(evaluations.size());

        if (evaluations.isEmpty()) {
            report.setAverageScore(0.0);
            report.setOverallRating("NO_DATA");
            report.setEvaluations(new ArrayList<>());
            report.setCriterionSummaries(new ArrayList<>());
            report.setStrengths(new ArrayList<>());
            report.setAreasForImprovement(new ArrayList<>());
            return report;
        }

        // Calcul du score moyen
        double totalScore = evaluations.stream()
                .mapToDouble(e -> e.getOverallScore() != null ? e.getOverallScore() : 0.0)
                .sum();
        double averageScore = totalScore / evaluations.size();
        report.setAverageScore(averageScore);
        report.setOverallRating(computeRating(averageScore));

        // Historique des évaluations
        List<PerformanceReportDto.EvaluationSummary> evalSummaries = evaluations.stream()
                .map(e -> {
                    PerformanceReportDto.EvaluationSummary summary = new PerformanceReportDto.EvaluationSummary();
                    summary.setEvaluationId(e.getId());
                    summary.setEvaluationDate(e.getEvaluationDate());
                    summary.setOverallScore(e.getOverallScore());
                    summary.setRating(e.getRating());
                    summary.setEvaluatorName(e.getEvaluatorName());
                    summary.setComments(e.getComments());
                    return summary;
                })
                .sorted((a, b) -> b.getEvaluationDate().compareTo(a.getEvaluationDate()))
                .collect(Collectors.toList());
        report.setEvaluations(evalSummaries);

        // Analyse par critère
        java.util.Map<Long, List<EmployeePerformanceDetail>> detailsByCriterion = new java.util.HashMap<>();
        for (EmployeePerformanceEvaluation eval : evaluations) {
            for (EmployeePerformanceDetail detail : eval.getDetails()) {
                detailsByCriterion.computeIfAbsent(detail.getCriterion().getId(), k -> new ArrayList<>())
                        .add(detail);
            }
        }

        List<PerformanceReportDto.CriterionSummary> criterionSummaries = new ArrayList<>();
        List<String> strengths = new ArrayList<>();
        List<String> areasForImprovement = new ArrayList<>();

        for (java.util.Map.Entry<Long, List<EmployeePerformanceDetail>> entry : detailsByCriterion.entrySet()) {
            List<EmployeePerformanceDetail> details = entry.getValue();
            if (details.isEmpty()) continue;

            PerformanceCriterion criterion = details.get(0).getCriterion();
            double avgScore = details.stream()
                    .mapToDouble(EmployeePerformanceDetail::getScore)
                    .average()
                    .orElse(0.0);
            double avgWeight = details.stream()
                    .mapToDouble(EmployeePerformanceDetail::getWeightUsed)
                    .average()
                    .orElse(0.0);

            PerformanceReportDto.CriterionSummary summary = new PerformanceReportDto.CriterionSummary();
            summary.setCriterionId(criterion.getId());
            summary.setCriterionCode(criterion.getCode());
            summary.setCriterionLabel(criterion.getLabel());
            summary.setCategory(criterion.getCategory());
            summary.setAverageScore(avgScore);
            summary.setWeight(avgWeight);
            summary.setEvaluationCount(details.size());
            criterionSummaries.add(summary);

            // Identifier les forces et faiblesses
            if (avgScore >= 85) {
                strengths.add(criterion.getLabel() + " (" + String.format("%.1f", avgScore) + "%)");
            } else if (avgScore < 60) {
                areasForImprovement.add(criterion.getLabel() + " (" + String.format("%.1f", avgScore) + "%)");
            }
        }

        report.setCriterionSummaries(criterionSummaries);
        report.setStrengths(strengths.isEmpty() ? List.of("Aucune force notable identifiée") : strengths);
        report.setAreasForImprovement(areasForImprovement.isEmpty() ?
                List.of("Aucun axe d'amélioration identifié") : areasForImprovement);

        // Commentaire général
        String generalComment = generateGeneralComment(averageScore, report.getOverallRating(), evaluations.size());
        report.setGeneralComment(generalComment);

        return report;
    }

    private String generateGeneralComment(double averageScore, String rating, int evaluationCount) {
        StringBuilder comment = new StringBuilder();
        comment.append("Basé sur ").append(evaluationCount).append(" évaluation(s), ");
        comment.append("le collaborateur obtient un score moyen de ").append(String.format("%.1f", averageScore)).append("%. ");

        switch (rating) {
            case "EXCELLENT":
                comment.append("Performance exceptionnelle. Le collaborateur excelle dans ses missions.");
                break;
            case "GOOD":
                comment.append("Bonne performance. Le collaborateur répond aux attentes de son poste.");
                break;
            case "AVERAGE":
                comment.append("Performance satisfaisante. Des marges d'amélioration sont possibles.");
                break;
            case "POOR":
                comment.append("Performance insuffisante. Un plan d'amélioration est recommandé.");
                break;
            default:
                comment.append("Évaluation en cours.");
        }

        return comment.toString();
    }
}
