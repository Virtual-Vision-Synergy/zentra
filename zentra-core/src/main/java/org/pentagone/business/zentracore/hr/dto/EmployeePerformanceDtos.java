package org.pentagone.business.zentracore.hr.dto;

import java.time.LocalDate;
import java.util.List;

public class EmployeePerformanceDtos {

    public static class PerformanceEvaluationPeriodDto {
        private Long id;
        private String code;
        private String label;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;
        // getters & setters
        // ...existing code...
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class EmployeePerformanceDetailDto {
        private Long criterionId;
        private String criterionCode;
        private String criterionLabel;
        private Double score;
        private Double weightUsed;
        private Double weightedScore;
        private String comment;
        // getters & setters
        // ...existing code...
        public Long getCriterionId() { return criterionId; }
        public void setCriterionId(Long criterionId) { this.criterionId = criterionId; }
        public String getCriterionCode() { return criterionCode; }
        public void setCriterionCode(String criterionCode) { this.criterionCode = criterionCode; }
        public String getCriterionLabel() { return criterionLabel; }
        public void setCriterionLabel(String criterionLabel) { this.criterionLabel = criterionLabel; }
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
        public Double getWeightUsed() { return weightUsed; }
        public void setWeightUsed(Double weightUsed) { this.weightUsed = weightUsed; }
        public Double getWeightedScore() { return weightedScore; }
        public void setWeightedScore(Double weightedScore) { this.weightedScore = weightedScore; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }

    public static class EmployeePerformanceEvaluationDto {
        private Long id;
        private Long employeeId;
        private Long periodId;
        private Double overallScore;
        private String rating;
        private String status;
        private LocalDate evaluationDate;
        private String evaluatorName;
        private String comments;
        private List<EmployeePerformanceDetailDto> details;
        // getters & setters
        // ...existing code...
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public Long getPeriodId() { return periodId; }
        public void setPeriodId(Long periodId) { this.periodId = periodId; }
        public Double getOverallScore() { return overallScore; }
        public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }
        public String getRating() { return rating; }
        public void setRating(String rating) { this.rating = rating; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalDate getEvaluationDate() { return evaluationDate; }
        public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }
        public String getEvaluatorName() { return evaluatorName; }
        public void setEvaluatorName(String evaluatorName) { this.evaluatorName = evaluatorName; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
        public List<EmployeePerformanceDetailDto> getDetails() { return details; }
        public void setDetails(List<EmployeePerformanceDetailDto> details) { this.details = details; }
    }

    public static class PerformanceEvaluationCreateRequest {
        private Long employeeId;
        private Long periodId;
        private String evaluatorName;
        private String comments;
        private java.util.List<EmployeePerformanceDetailDto> details;
        // getters & setters
        // ...existing code...
        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public Long getPeriodId() { return periodId; }
        public void setPeriodId(Long periodId) { this.periodId = periodId; }
        public String getEvaluatorName() { return evaluatorName; }
        public void setEvaluatorName(String evaluatorName) { this.evaluatorName = evaluatorName; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
        public java.util.List<EmployeePerformanceDetailDto> getDetails() { return details; }
        public void setDetails(java.util.List<EmployeePerformanceDetailDto> details) { this.details = details; }
    }

    public static class AutoScoringRequest {
        private Long periodId;
        private java.util.List<Long> employeeIds;
        // getters & setters
        // ...existing code...
        public Long getPeriodId() { return periodId; }
        public void setPeriodId(Long periodId) { this.periodId = periodId; }
        public java.util.List<Long> getEmployeeIds() { return employeeIds; }
        public void setEmployeeIds(java.util.List<Long> employeeIds) { this.employeeIds = employeeIds; }
    }
}

