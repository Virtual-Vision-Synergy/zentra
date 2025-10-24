package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CandidateDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String country;
    private String educationLevel;
    private String lastDegree;
    private Integer yearsExperience;
    private String skills;
    private String cvFile;
    private String motivationalLetterFile;
}
