package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;


import java.time.LocalDate;

@Data
public class QcmDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private Integer durationMinutes;
    private Double totalScore;
    private Double requiredScore;
}
