package org.pentagone.business.zentracore.hr.ai.chatbot.repository;

import org.pentagone.business.zentracore.hr.ai.chatbot.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);
    List<ChatMessage> findByUserId(Integer userId);
}

