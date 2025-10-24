package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.util.List;

@Entity
@Table(name = "choice")
@Data
@EqualsAndHashCode(callSuper = true)
public class Choice extends BaseEntity {
    
    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;
    
    @Column(name = "correct")
    private boolean correct = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
