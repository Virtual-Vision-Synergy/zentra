package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "publication")
@Data
@EqualsAndHashCode(callSuper = true)
public class Publication extends BaseEntity {
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;
    
    @Column(name = "closing_date")
    private LocalDate closingDate;
    
    @Column(name = "number_of_positions", nullable = false)
    private Integer numberOfPositions = 1;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status = "Open";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;
}
