package org.pentagone.business.zentracore.hr.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.time.LocalDateTime;
import java.util.List;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;
    
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Interview> interviews;
    
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attempt> attempts;
}
