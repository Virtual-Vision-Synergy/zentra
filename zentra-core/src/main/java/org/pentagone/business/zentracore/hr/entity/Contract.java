package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employment_contract")
@Data
@EqualsAndHashCode(callSuper = true)
public class Contract extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(name = "contract_number", nullable = false, unique = true, length = 100)
    private String contractNumber;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "gross_salary", precision = 10, scale = 2)
    private BigDecimal grossSalary;
    
    @Column(name = "annual_bonus", precision = 10, scale = 2)
    private BigDecimal annualBonus;
    
    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;
    
    @Column(name = "weekly_hours", nullable = false)
    private Double weeklyHours;
    
    @Column(name = "annual_leave_days", nullable = false)
    private Integer annualLeaveDays = 25;
    
    @Column(name = "signature_date")
    private LocalDate signatureDate;
    
    @Column(name = "contract_file", length = 255)
    private String contractFile;
}
