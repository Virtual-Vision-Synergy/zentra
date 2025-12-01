package org.pentagone.business.zentracore.hr.ai.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedDocumentDTO {
    private Long id;
    private String documentType;
    private Integer employeeId;
    private String employeeName;
    private String filePath;
    private String fileName;
    private LocalDateTime generatedAt;
    private Integer generatedBy;
}

