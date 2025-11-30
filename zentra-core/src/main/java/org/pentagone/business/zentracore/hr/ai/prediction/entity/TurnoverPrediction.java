package org.pentagone.business.zentracore.hr.ai.prediction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_turnover_predictions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer employeeId;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private Double riskScore;

    @Column(nullable = false)
    private String riskLevel;

    @Column(columnDefinition = "TEXT")
    private String reasons;

    @Column(nullable = false)
    private LocalDateTime predictedAt;

    @PrePersist
    protected void onCreate() {
        predictedAt = LocalDateTime.now();
    }
}

