package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "candidate")
@Data
@EqualsAndHashCode(callSuper = true)
public class Candidate extends BaseEntity {
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "country", nullable = false, length = 100)
    private String country = "France";
    
    @Column(name = "education_level", length = 100)
    private String educationLevel;
    
    @Column(name = "last_degree", length = 150)
    private String lastDegree;
    
    @Column(name = "years_experience", nullable = false)
    private Integer yearsExperience = 0;
    
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;
    
    @Column(name = "cv_file", length = 255)
    private String cvFile;
    
    @Column(name = "motivational_letter_file", length = 255)
    private String motivationalLetterFile;
    
    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    private Application application;
}
