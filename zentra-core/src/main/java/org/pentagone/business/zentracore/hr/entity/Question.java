package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.util.List;

@Entity
@Table(name = "question")
@Data
@EqualsAndHashCode(callSuper = true)
public class Question extends BaseEntity {
    
    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;
    
    @Column(name = "required")
    private boolean required = true;
    
    @Column(name = "score")
    private Double score;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qcm_id", nullable = false)
    private Qcm qcm;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Choice> choices;
}
