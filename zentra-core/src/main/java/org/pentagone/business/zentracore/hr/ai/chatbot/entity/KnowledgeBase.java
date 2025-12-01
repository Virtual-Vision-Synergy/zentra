package org.pentagone.business.zentracore.hr.ai.chatbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_chatbot_knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category; // LEAVE, PAYROLL, ATTENDANCE, CONTRACT, GENERAL

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(columnDefinition = "TEXT")
    private String keywords; // Comma-separated keywords for matching

    private Boolean active = true;
}

