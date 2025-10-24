package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;


import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InterviewDto {
    private Long id;
    private String interviewType;
    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private Long interviewerId;
    private String report;
    private Double score;
    private Long applicationId;
}
