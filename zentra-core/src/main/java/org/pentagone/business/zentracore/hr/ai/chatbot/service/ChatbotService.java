package org.pentagone.business.zentracore.hr.ai.chatbot.service;
import java.util.stream.Collectors;
import java.util.*;
import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.pentagone.business.zentracore.hr.ai.chatbot.repository.KnowledgeBaseRepository;
import org.pentagone.business.zentracore.hr.ai.chatbot.repository.ChatMessageRepository;
import org.pentagone.business.zentracore.hr.ai.chatbot.entity.KnowledgeBase;
import org.pentagone.business.zentracore.hr.ai.chatbot.entity.ChatMessage;
import org.pentagone.business.zentracore.hr.ai.chatbot.dto.ChatRequestDTO;
import org.pentagone.business.zentracore.hr.ai.chatbot.dto.ChatMessageDTO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
@Slf4j
@RequiredArgsConstructor
@Service

public class ChatbotService {

    private final ChatMessageRepository chatMessageRepository;
    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final OpenAIService openAIService;

    @Value("${ai.chatbot.use-openai:false}")
    private boolean useOpenAI;

    @Transactional
    public ChatMessageDTO processMessage(ChatRequestDTO request) {
        log.info("Processing message: {} from session: {}", request.getMessage(), request.getSessionId());

        String response;
        String category = "GENERAL";

        try {
            // Try to find answer from knowledge base first
            response = findFromKnowledgeBase(request.getMessage());

            // Fallback to OpenAI if no answer found and OpenAI is enabled
            if (response == null && useOpenAI) {
                response = openAIService.getChatResponse(request.getMessage(), getConversationHistory(request.getSessionId()));
                category = categorizeMessage(request.getMessage());
            } else if (response == null) {
                response = "Je suis désolé, je n'ai pas trouvé de réponse précise à votre question. " +
                           "Veuillez contacter le service RH pour plus d'informations.";
            } else {
                category = categorizeMessage(request.getMessage());
            }
        } catch (Exception e) {
            log.error("Error processing message", e);
            response = "Une erreur s'est produite. Veuillez réessayer.";
        }

        // Save conversation
        ChatMessage message = new ChatMessage();
        message.setSessionId(request.getSessionId());
        message.setMessage(request.getMessage());
        message.setResponse(response);
        message.setCategory(category);
        message.setUserId(request.getUserId());
        message.setTimestamp(LocalDateTime.now());

        ChatMessage saved = chatMessageRepository.save(message);

        return convertToDTO(saved);
    }

    private String findFromKnowledgeBase(String message) {

        // Search in knowledge base
        List<KnowledgeBase> allKnowledge = knowledgeBaseRepository.findByActiveTrue();

        // Score each knowledge entry
        Map<KnowledgeBase, Double> scores = new HashMap<>();
        for (KnowledgeBase kb : allKnowledge) {
            double score = calculateRelevanceScore(message.toLowerCase(), kb);
            if (score > 0.3) { // Threshold
                scores.put(kb, score);
            }
        }

        if (!scores.isEmpty()) {
            // Return the best match
            KnowledgeBase bestMatch = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

            if (bestMatch != null) {
                log.info("Found answer in knowledge base: {}", bestMatch.getId());
                return bestMatch.getAnswer();
            }
        }

        return null;
    }

    private double calculateRelevanceScore(String message, KnowledgeBase kb) {
        String[] messageWords = message.split("\\s+");
        String combinedKB = (kb.getQuestion() + " " + kb.getKeywords()).toLowerCase();

        int matches = 0;
        for (String word : messageWords) {
            if (word.length() > 3 && combinedKB.contains(word)) {
                matches++;
            }
        }

        return (double) matches / messageWords.length;
    }

    private String categorizeMessage(String message) {
        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("congé") || lowerMessage.contains("vacances") ||
            lowerMessage.contains("absence") || lowerMessage.contains("leave")) {
            return "LEAVE";
        } else if (lowerMessage.contains("salaire") || lowerMessage.contains("paie") ||
                   lowerMessage.contains("payroll") || lowerMessage.contains("rémunération")) {
            return "PAYROLL";
        } else if (lowerMessage.contains("présence") || lowerMessage.contains("pointage") ||
                   lowerMessage.contains("attendance") || lowerMessage.contains("horaire")) {
            return "ATTENDANCE";
        } else if (lowerMessage.contains("contrat") || lowerMessage.contains("contract") ||
                   lowerMessage.contains("embauche")) {
            return "CONTRACT";
        }

        return "GENERAL";
    }

    private List<ChatMessage> getConversationHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }

    public List<ChatMessageDTO> getSessionHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ChatMessageDTO> getUserHistory(Integer userId) {
        return chatMessageRepository.findByUserId(userId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setSessionId(message.getSessionId());
        dto.setMessage(message.getMessage());
        dto.setResponse(message.getResponse());
        dto.setCategory(message.getCategory());
        dto.setTimestamp(message.getTimestamp());
        dto.setUserId(message.getUserId());
        return dto;
    }

    // Admin methods for managing knowledge base
    @Transactional
    public KnowledgeBase addKnowledge(KnowledgeBase knowledge) {
        return knowledgeBaseRepository.save(knowledge);
    }

    public List<KnowledgeBase> getAllKnowledge() {
        return knowledgeBaseRepository.findAll();
    }

    @Transactional
    public void deleteKnowledge(Long id) {
        knowledgeBaseRepository.deleteById(id);
    }
}