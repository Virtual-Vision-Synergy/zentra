package org.pentagone.business.zentracore.hr.ai.document.dto;

import java.util.Map;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentGenerationRequest {
    private Map<String, Object> additionalData;
    private Integer employeeId;
    private String documentType; // CONTRACT, ATTESTATION, PAYSLIP, CERTIFICATE


}





