package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InterviewDto {
    private Long id;
    private Long candidateId;
    private String candidateName; // Pour l'affichage
    private String candidateEmail; // Pour l'affichage
    private Long interviewerId;
    private String interviewerName; // Pour l'affichage
    private LocalDate interviewDate;
    private LocalTime startTime;
    private Integer durationMinutes;
    private String interviewType; // PRESENTIEL, VISIO, TELEPHONIQUE
    private String location; // Lieu ou lien visio
    private String status; // PLANIFIE, REALISE, ANNULE
    private String comment;
    private Double score;
    private Long applicationId;
}
