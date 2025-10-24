package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicationDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate publishedDate;
    private LocalDate closingDate;
    private Integer numberOfPositions;
    private String status;
    private Long jobId;
}
