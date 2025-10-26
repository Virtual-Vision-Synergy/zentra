package org.pentagone.business.zentracore.hr.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Data
@EqualsAndHashCode(callSuper = true)
public class Application extends BaseEntity {
    
    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status = "Received";
    
    @Column(name = "document_score")
    private Double documentScore;
    
    @Column(name = "score")
    private Double score;
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Interview interview;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Attempt attempt;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Token token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qcm_id")
    private Qcm qcm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id")
    private Publication publication;
}
