package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_performance_detail")
public class EmployeePerformanceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evaluation_id")
    private EmployeePerformanceEvaluation evaluation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "criterion_id")
    private PerformanceCriterion criterion;

    @Column(nullable = false)
    private Double score; // e.g. 0-100

    @Column(nullable = false)
    private Double weightUsed;

    @Column(nullable = false)
    private Double weightedScore;

    @Column(length = 1000)
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeePerformanceEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EmployeePerformanceEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public PerformanceCriterion getCriterion() {
        return criterion;
    }

    public void setCriterion(PerformanceCriterion criterion) {
        this.criterion = criterion;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getWeightUsed() {
        return weightUsed;
    }

    public void setWeightUsed(Double weightUsed) {
        this.weightUsed = weightUsed;
    }

    public Double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(Double weightedScore) {
        this.weightedScore = weightedScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

