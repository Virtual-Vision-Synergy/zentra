package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private String libelle;
    private boolean required;
    private Double score;
    private Long qcmId;
    
    private List<ChoiceDto> choices;
}
