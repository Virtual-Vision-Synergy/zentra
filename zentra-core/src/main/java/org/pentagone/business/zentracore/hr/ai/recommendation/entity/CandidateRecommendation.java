package org.pentagone.business.zentracore.hr.ai.recommendation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_candidate_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer candidateId;

    @Column(nullable = false)
    private String candidateName;

    @Column(nullable = false)
    private Integer jobId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private Double matchScore;

    @Column(columnDefinition = "TEXT")
    private String matchDetails;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;

    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
    }
}

