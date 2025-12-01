package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee_performance_evaluation")
public class EmployeePerformanceEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "period_id")
    private PerformanceEvaluationPeriod period;

    @Column
    private Double overallScore;

    @Column(length = 50)
    private String rating; // EXCELLENT, GOOD, AVERAGE, POOR

    @Column(length = 50)
    private String status; // DRAFT, SUBMITTED, VALIDATED, AUTO_GENERATED

    @Column
    private LocalDate evaluationDate;

    @Column(length = 255)
    private String evaluatorName;

    @Column(length = 2000)
    private String comments;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeePerformanceDetail> details = new ArrayList<>();

    public void addDetail(EmployeePerformanceDetail detail) {
        details.add(detail);
        detail.setEvaluation(this);
    }

    public void removeDetail(EmployeePerformanceDetail detail) {
        details.remove(detail);
        detail.setEvaluation(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PerformanceEvaluationPeriod getPeriod() {
        return period;
    }

    public void setPeriod(PerformanceEvaluationPeriod period) {
        this.period = period;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
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

    public List<EmployeePerformanceDetail> getDetails() {
        return details;
    }

    public void setDetails(List<EmployeePerformanceDetail> details) {
        this.details = details;
    }
}

