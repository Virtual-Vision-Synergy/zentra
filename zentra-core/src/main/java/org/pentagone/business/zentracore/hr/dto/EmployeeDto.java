package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;


import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long id;
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String workEmail;
    private String workPhone;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String city;
    private String country;
    private LocalDate hireDate;
    private Double baseSalary;
    private LocalDate contractEndDate;
    private Long candidateId;
    private Long jobId;
}
