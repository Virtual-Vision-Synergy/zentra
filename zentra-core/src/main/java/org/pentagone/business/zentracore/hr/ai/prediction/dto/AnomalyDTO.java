package org.pentagone.business.zentracore.hr.ai.prediction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyDTO {
    private Long id;
    private String anomalyType; // ATTENDANCE, PAYROLL, HOURS
    private Integer employeeId;
    private String employeeName;
    private String description;
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private java.time.LocalDateTime detectedAt;
    private Boolean resolved;
    private String dataReference; // Reference to the anomalous data
}

