package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StaffingNeedDto {
    private Long id;
    private String title;
    private String description;
    private Integer numberOfPositions;
    private String priority;
    private String status;
    private LocalDate requiredStartDate;
    private BigDecimal budgetAllocated;
    private String justification;
    private Long departmentId;
    private String departmentName;
    private Long jobId;
    private String jobTitle;
    private Long requestedById;
    private String requestedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
