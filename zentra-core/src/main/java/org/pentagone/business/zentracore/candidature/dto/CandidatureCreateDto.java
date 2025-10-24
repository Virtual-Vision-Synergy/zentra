package org.pentagone.business.zentracore.candidature.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureCreateDto {
    private Long idCandidat;
    private Long idAnnonce;
    private BigDecimal scoreInitial;
    private String commentaireInitial;
}
