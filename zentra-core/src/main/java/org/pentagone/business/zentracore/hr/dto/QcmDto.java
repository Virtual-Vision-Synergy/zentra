package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Data
public class QcmDto {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Double totalScore;
    private Double requiredScore;

    private List<QuestionDto> questions;
}
