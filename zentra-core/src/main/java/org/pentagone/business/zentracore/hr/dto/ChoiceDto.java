package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

@Data
public class ChoiceDto {
    private Long id;
    private String libelle;
    private boolean correct;
    private Long questionId;
    
    
}
