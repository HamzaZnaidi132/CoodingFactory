package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.config.OllamaProperties;
import com.codingfactory.chatbot.dto.ConversationTurnDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OllamaChatClient {

    private static final Logger log = LoggerFactory.getLogger(OllamaChatClient.class);

    private final OllamaProperties properties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public OllamaChatClient(OllamaProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(Math.max(30, properties.getTimeoutSeconds())));
        this.restClient = RestClient.builder()
                .baseUrl(trimTrailingSlash(properties.getBaseUrl()))
                .requestFactory(factory)
                .build();
    }

    public Optional<String> chat(String systemPrompt, List<ConversationTurnDto> history, String userMessage) {
        if (!properties.isEnabled()) {
            return Optional.empty();
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        for (ConversationTurnDto turn : history) {
            messages.add(Map.of("role", turn.role(), "content", turn.content()));
        }
        messages.add(Map.of("role", "user", "content", userMessage));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", properties.getModel());
        body.put("messages", messages);
        body.put("stream", false);

        try {
            String raw = restClient.post()
                    .uri("/api/chat")
                    .body(body)
                    .retrieve()
                    .body(String.class);

            if (raw == null || raw.isBlank()) {
                return Optional.empty();
            }
            JsonNode root = objectMapper.readTree(raw);
            JsonNode content = root.path("message").path("content");
            if (content.isMissingNode() || content.asText().isBlank()) {
                return Optional.empty();
            }
            return Optional.of(content.asText().trim());
        } catch (RestClientException | com.fasterxml.jackson.core.JsonProcessingException ex) {
            log.warn("Ollama indisponible, repli sur le moteur local: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    public JsonNode listModels() {
        return restClient.get()
                .uri("/api/tags")
                .retrieve()
                .body(JsonNode.class);
    }

    public JsonNode rawChat(Map<String, Object> body) {
        return restClient.post()
                .uri("/api/chat")
                .body(body)
                .retrieve()
                .body(JsonNode.class);
    }

    private static String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            return "http://localhost:11434";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
