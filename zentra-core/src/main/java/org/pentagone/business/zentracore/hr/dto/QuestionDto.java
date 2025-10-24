package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;



@Data
public class QuestionDto {
    private Long id;
    private String libelle;
    private Boolean required;
    private Double score;
    private Long qcmId;
}
