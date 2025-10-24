package org.pentagone.business.zentracore.candidature.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureResponseDto {
    private Long idCandidature;
    private Long idCandidat;
    private String nomCandidat;
    private String prenomCandidat;
    private String emailCandidat;
    private Long idAnnonce;
    private String titreAnnonce;
    private LocalDateTime dateCandidature;
    private String statut;
    private BigDecimal scoreInitial;
    private String commentaireInitial;
    private LocalDateTime dateDerniereModification;
}
