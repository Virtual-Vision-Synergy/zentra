package org.pentagone.business.zentracore.hr.ai.prediction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_anomaly_detections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anomaly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String anomalyType;

    @Column(nullable = false)
    private Integer employeeId;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private LocalDateTime detectedAt;

    @Column(nullable = false)
    private Boolean resolved = false;

    private String dataReference;

    @PrePersist
    protected void onCreate() {
        detectedAt = LocalDateTime.now();
        if (resolved == null) {
            resolved = false;
        }
    }
}

