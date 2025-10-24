package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;



@Data
public class ChoiceDto {
    private Long id;
    private String libelle;
    private Double score;
    private Boolean correct;
    private Long questionId;
}
