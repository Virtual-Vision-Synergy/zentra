package org.pentagone.business.zentracore.hr.dto;

import java.time.LocalDate;
import java.util.List;

public class PerformanceReportDto {
    private Long employeeId;
    private String employeeName;
    private String employeePosition;
    private Long periodId;
    private String periodLabel;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate generatedDate;

    // Statistiques globales
    private Double averageScore;
    private String overallRating;
    private Integer totalEvaluations;

    // Détails par critère
    private List<CriterionSummary> criterionSummaries;

    // Historique des évaluations
    private List<EvaluationSummary> evaluations;

    // Recommandations
    private List<String> strengths;
    private List<String> areasForImprovement;
    private String generalComment;

    public static class CriterionSummary {
        private Long criterionId;
        private String criterionCode;
        private String criterionLabel;
        private String category;
        private Double averageScore;
        private Double weight;
        private Integer evaluationCount;

        // Getters et Setters
        public Long getCriterionId() {
            return criterionId;
        }

        public void setCriterionId(Long criterionId) {
            this.criterionId = criterionId;
        }

        public String getCriterionCode() {
            return criterionCode;
        }

        public void setCriterionCode(String criterionCode) {
            this.criterionCode = criterionCode;
        }

        public String getCriterionLabel() {
            return criterionLabel;
        }

        public void setCriterionLabel(String criterionLabel) {
            this.criterionLabel = criterionLabel;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Double getAverageScore() {
            return averageScore;
        }

        public void setAverageScore(Double averageScore) {
            this.averageScore = averageScore;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Integer getEvaluationCount() {
            return evaluationCount;
        }

        public void setEvaluationCount(Integer evaluationCount) {
            this.evaluationCount = evaluationCount;
        }
    }

    public static class EvaluationSummary {
        private Long evaluationId;
        private LocalDate evaluationDate;
        private Double overallScore;
        private String rating;
        private String evaluatorName;
        private String comments;

        // Getters et Setters
        public Long getEvaluationId() {
            return evaluationId;
        }

        public void setEvaluationId(Long evaluationId) {
            this.evaluationId = evaluationId;
        }

        public LocalDate getEvaluationDate() {
            return evaluationDate;
        }

        public void setEvaluationDate(LocalDate evaluationDate) {
            this.evaluationDate = evaluationDate;
        }

        public Double getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(Double overallScore) {
            this.overallScore = overallScore;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getEvaluatorName() {
            return evaluatorName;
        }

        public void setEvaluatorName(String evaluatorName) {
            this.evaluatorName = evaluatorName;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }
    }

    // Getters et Setters principaux
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public Integer getTotalEvaluations() {
        return totalEvaluations;
    }

    public void setTotalEvaluations(Integer totalEvaluations) {
        this.totalEvaluations = totalEvaluations;
    }

    public List<CriterionSummary> getCriterionSummaries() {
        return criterionSummaries;
    }

    public void setCriterionSummaries(List<CriterionSummary> criterionSummaries) {
        this.criterionSummaries = criterionSummaries;
    }

    public List<EvaluationSummary> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationSummary> evaluations) {
        this.evaluations = evaluations;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getAreasForImprovement() {
        return areasForImprovement;
    }

    public void setAreasForImprovement(List<String> areasForImprovement) {
        this.areasForImprovement = areasForImprovement;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }
}

