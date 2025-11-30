package org.pentagone.business.zentracore.hr.ai.chatbot.dto;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageDTO {
    private Integer userId;
    private LocalDateTime timestamp;
    private String category; // LEAVE, PAYROLL, ATTENDANCE, GENERAL
    private String response;
    private String message;
    private String sessionId;
    private Long id;
}





