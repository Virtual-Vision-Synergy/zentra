package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ApplicationDto {
    private Long id;
    private LocalDateTime appliedAt;
    private String status;
    private Double documentScore;
    private Double score;
    private String comment;
    private Long candidateId;
    private Long publicationId;
}
