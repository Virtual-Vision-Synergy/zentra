package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class DepartmentDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal annualBudget;
}
