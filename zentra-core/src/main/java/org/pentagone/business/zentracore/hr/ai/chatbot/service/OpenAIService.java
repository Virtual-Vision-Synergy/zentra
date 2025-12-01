package org.pentagone.business.zentracore.hr.ai.chatbot.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OpenAIService {

    @Value("${ai.openai.api-key:}")
    private String apiKey;

    @Value("${ai.openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${ai.chatbot.enabled:false}")
    private boolean enabled;

    public String getChatResponse(String userMessage, List<org.pentagone.business.zentracore.hr.ai.chatbot.entity.ChatMessage> history) {
        if (!enabled || apiKey == null || apiKey.isEmpty()) {
            log.warn("OpenAI is not enabled or API key is missing");
            return "Le service de chatbot IA n'est pas disponible actuellement.";
        }

        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

            List<ChatMessage> messages = new ArrayList<>();

            // System message to set the context
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Tu es un assistant RH spécialisé pour l'entreprise Zentra. " +
                "Tu aides les employés avec des questions sur les congés, la paie, les horaires, " +
                "les contrats et autres sujets RH. Réponds de manière professionnelle, claire et concise en français."));

            // Add conversation history (last 5 messages for context)
            if (history != null && !history.isEmpty()) {
                int startIndex = Math.max(0, history.size() - 5);
                for (int i = startIndex; i < history.size(); i++) {
                    org.pentagone.business.zentracore.hr.ai.chatbot.entity.ChatMessage msg = history.get(i);
                    messages.add(new ChatMessage(ChatMessageRole.USER.value(), msg.getMessage()));
                    messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), msg.getResponse()));
                }
            }

            // Add current message
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));

            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .temperature(0.7)
                    .maxTokens(500)
                    .build();

            String response = service.createChatCompletion(completionRequest)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            log.info("OpenAI response received");
            return response;

        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            return "Je suis désolé, je n'ai pas pu traiter votre demande. Veuillez réessayer ou contacter le service RH.";
        }
    }

    public String extractSkillsFromCV(String cvText) {
        if (!enabled || apiKey == null || apiKey.isEmpty()) {
            log.warn("OpenAI is not enabled for skill extraction");
            return "";
        }

        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));

            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Tu es un expert en analyse de CV. Extrais les compétences techniques et professionnelles " +
                "d'un CV et retourne-les sous forme de liste séparée par des virgules."));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(),
                "Extrait les compétences de ce CV:\n\n" + cvText));

            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .temperature(0.3)
                    .maxTokens(300)
                    .build();

            return service.createChatCompletion(completionRequest)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

        } catch (Exception e) {
            log.error("Error extracting skills from CV", e);
            return "";
        }
    }
}

