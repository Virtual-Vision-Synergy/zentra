package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;


import java.time.LocalDate;

@Data
public class ContractDto {
    private Long id;
    private Long employeeId;
    private String contractNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double grossSalary;
    private Double annualBonus;
    private String benefits;
    private Double weeklyHours;
    private Integer annualLeaveDays;
    private LocalDate signatureDate;
    private String contractFile;
}
