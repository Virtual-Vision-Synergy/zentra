package org.pentagone.business.zentracore.hr.ai.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {
    private String message;
    private String sessionId;
    private Integer userId;
}

