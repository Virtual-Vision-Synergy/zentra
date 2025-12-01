package org.pentagone.business.zentracore.hr.ai.document.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_generated_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private Integer employeeId;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    private Integer generatedBy;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}

