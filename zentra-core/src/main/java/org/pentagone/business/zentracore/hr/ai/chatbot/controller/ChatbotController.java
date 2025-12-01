package org.pentagone.business.zentracore.hr.ai.chatbot.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.chatbot.dto.ChatMessageDTO;
import org.pentagone.business.zentracore.hr.ai.chatbot.dto.ChatRequestDTO;
import org.pentagone.business.zentracore.hr.ai.chatbot.entity.KnowledgeBase;
import org.pentagone.business.zentracore.hr.ai.chatbot.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatMessageDTO> chat(@RequestBody ChatRequestDTO request) {
        ChatMessageDTO response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/session/{sessionId}")
    public ResponseEntity<List<ChatMessageDTO>> getSessionHistory(@PathVariable String sessionId) {
        List<ChatMessageDTO> history = chatbotService.getSessionHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<ChatMessageDTO>> getUserHistory(@PathVariable Integer userId) {
        List<ChatMessageDTO> history = chatbotService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }

    // Admin endpoints for knowledge base management
    @PostMapping("/admin/knowledge")
    public ResponseEntity<KnowledgeBase> addKnowledge(@RequestBody KnowledgeBase knowledge) {
        KnowledgeBase saved = chatbotService.addKnowledge(knowledge);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/admin/knowledge")
    public ResponseEntity<List<KnowledgeBase>> getAllKnowledge() {
        List<KnowledgeBase> knowledge = chatbotService.getAllKnowledge();
        return ResponseEntity.ok(knowledge);
    }

    @DeleteMapping("/admin/knowledge/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Long id) {
        chatbotService.deleteKnowledge(id);
        return ResponseEntity.noContent().build();
    }

    @PostConstruct
    public void init() {
        log.info("========================================");
        log.info("✅ ChatbotController INITIALIZED");
        log.info("📍 Base path: /chatbot");
        log.info("🌐 Full URL: http://localhost:8080/api/chatbot");
        log.info("========================================");
    }
}
