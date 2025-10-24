package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;



@Data
public class DepartmentDto {
    private Long id;
    private String name;
    private String description;
    private Double annualBudget;
}
