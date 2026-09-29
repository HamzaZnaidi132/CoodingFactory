package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.dto.ConversationTurnDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaClient {

    private final RestClient restClient;
    private final String model;
    private final boolean enabled;
    private final double temperature;
    private final String systemPrompt;

    public OllamaClient(
            RestClient.Builder restClientBuilder,
            @Value("${ollama.base-url:http://localhost:11434}") String baseUrl,
            @Value("${ollama.model:llama3.2}") String model,
            @Value("${ollama.enabled:true}") boolean enabled,
            @Value("${ollama.temperature:0.35}") double temperature,
            @Value("${ollama.system-prompt}") String systemPrompt
    ) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.model = model;
        this.enabled = enabled;
        this.temperature = temperature;
        this.systemPrompt = systemPrompt;
    }

    public String generate(String message, List<ConversationTurnDto> history, String serviceContext) {
        if (!enabled) {
            return null;
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt + "\n\nContexte de routage: " + serviceContext));
        if (history != null) {
            history.stream()
                    .filter(turn -> turn != null && turn.content() != null && !turn.content().isBlank())
                    .filter(turn -> "user".equals(turn.role()) || "assistant".equals(turn.role()))
                    .limit(12)
                    .forEach(turn -> messages.add(Map.of(
                            "role", turn.role(),
                            "content", turn.content().trim()
                    )));
        }
        messages.add(Map.of("role", "user", "content", message.trim()));

        Map<String, Object> options = new HashMap<>();
        options.put("temperature", temperature);

        try {
            OllamaResponse response = restClient.post()
                    .uri("/api/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "model", model,
                            "messages", messages,
                            "stream", false,
                            "options", options
                    ))
                    .retrieve()
                    .body(OllamaResponse.class);
            if (response == null || response.message() == null || response.message().content() == null) {
                return null;
            }
            String content = response.message().content().trim();
            return content.isBlank() ? null : content;
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public record OllamaResponse(OllamaMessage message) {
    }

    public record OllamaMessage(String role, String content) {
    }
}
