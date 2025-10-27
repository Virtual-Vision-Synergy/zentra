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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private Employee interviewer;

    @Column(name = "interview_date", nullable = false)
    private LocalDate interviewDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    
    @Column(name = "interview_type", nullable = false, length = 50)
    private String interviewType; // PRESENTIEL, VISIO, TELEPHONIQUE

    @Column(name = "location", columnDefinition = "TEXT")
    private String location; // Lieu physique ou lien visio

    @Column(name = "status", nullable = false, length = 50)
    private String status = "PLANIFIE"; // PLANIFIE, REALISE, ANNULE

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "score")
    private Double score;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    private Application application;
}
