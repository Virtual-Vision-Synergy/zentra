package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.util.List;

@Entity
@Table(name = "department")
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {
    
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "annual_budget")
    private Double annualBudget;
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Job> jobs;
}
