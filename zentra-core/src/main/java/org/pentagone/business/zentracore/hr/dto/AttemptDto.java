package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;



@Data
public class AttemptDto {
    private Long id;
    private Long qcmId;
    private Long applicationId;
    private Double obtainedScore;
}
