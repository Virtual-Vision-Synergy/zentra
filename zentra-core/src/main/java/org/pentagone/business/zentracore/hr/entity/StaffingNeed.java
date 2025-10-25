package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "staffing_need")
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffingNeed extends BaseEntity {
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "number_of_positions", nullable = false)
    private Integer numberOfPositions;
    
    @Column(name = "priority", length = 50)
    private String priority; // High, Medium, Low
    
    @Column(name = "status", nullable = false, length = 50)
    private String status; // Open, In Progress, Fulfilled, Cancelled
    
    @Column(name = "required_start_date")
    private LocalDate requiredStartDate;
    
    @Column(name = "budget_allocated", precision = 15, scale = 2)
    private BigDecimal budgetAllocated;
    
    @Column(name = "justification", columnDefinition = "TEXT")
    private String justification;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by")
    private Employee requestedBy;
}
