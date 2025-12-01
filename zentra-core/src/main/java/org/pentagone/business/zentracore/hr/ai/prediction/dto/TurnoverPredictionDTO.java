package org.pentagone.business.zentracore.hr.ai.prediction.dto;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TurnoverPredictionDTO {
    private java.time.LocalDateTime predictedAt;
    private String reasons;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private Double riskScore; // 0.0 to 1.0
    private String employeeName;
    private Integer employeeId;
}





