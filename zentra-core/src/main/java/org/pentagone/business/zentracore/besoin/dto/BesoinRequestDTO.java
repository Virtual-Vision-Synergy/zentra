package org.pentagone.business.zentracore.besoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinRequestDTO {
    private String titre;
    private String description;
    private String departement;
    private Integer nombrePostes;
    private String typeContrat;
    private LocalDate dateLimite;
    private String statut;
    private String priorite;
    private String competencesRequises;
    private Double budgetAlloue;
}
