package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "qcm")
@Data
@EqualsAndHashCode(callSuper = true)
public class Qcm extends BaseEntity {
    
    @Column(name = "title", nullable = false, length = 150)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "total_score")
    private Double totalScore;
    
    @Column(name = "required_score")
    private Double requiredScore;
    
    @OneToMany(mappedBy = "qcm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;
    
    @OneToMany(mappedBy = "qcm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attempt> attempts;
}
