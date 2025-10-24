package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "interview")
@Data
@EqualsAndHashCode(callSuper = true)
public class Interview extends BaseEntity {
    
    @Column(name = "interview_type", nullable = false, length = 100)
    private String interviewType;
    
    @Column(name = "interview_date", nullable = false)
    private LocalDate interviewDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private Employee interviewer;
    
    @Column(name = "report", columnDefinition = "TEXT")
    private String report;
    
    @Column(name = "score")
    private Double score;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
}
