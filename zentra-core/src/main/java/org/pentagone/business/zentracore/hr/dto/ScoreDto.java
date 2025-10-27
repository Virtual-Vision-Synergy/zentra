package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

@Data
public class ScoreDto {
    private Double finalScore;
    private Double documentScore;
    private Double qcmScore;
    private Double interviewScore;
}
