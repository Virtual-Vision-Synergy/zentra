package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

@Data
public class CandidateMinInfoDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long applicationId;
}

