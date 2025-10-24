package org.pentagone.business.zentracore.qcm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEvaluationDTO {
    
    private Long idTest;
    private Long idCandidature;
    private String typeTest;
    private String nomTest;
    private String description;
    private LocalDate dateTest;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer dureeMinutes;
    private BigDecimal scoreObtenu;
    private BigDecimal scoreRequis;
    private Boolean reussi;
    private String commentaire;
    private String evaluateur;
}
