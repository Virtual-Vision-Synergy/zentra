package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

@Data
public class JobDto {
    private Long id;
    private String title;
    private String description;
    private String requiredDegree;
    private String requiredSkills;
    private Long departmentId;
}
