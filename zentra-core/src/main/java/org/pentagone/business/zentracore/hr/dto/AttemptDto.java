package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.util.List;


@Data
public class AttemptDto {
    private Long id;
    private Long applicationId;
    private Double obtainedScore;

    private List<AnswerDto> answers;
}
