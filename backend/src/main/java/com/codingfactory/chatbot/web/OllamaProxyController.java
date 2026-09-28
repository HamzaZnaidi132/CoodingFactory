package com.codingfactory.chatbot.web;

import com.codingfactory.chatbot.config.OllamaProperties;
import com.codingfactory.chatbot.dto.OllamaChatProxyRequest;
import com.codingfactory.chatbot.service.OllamaChatClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ollama")
public class OllamaProxyController {

    private final OllamaChatClient ollamaChatClient;
    private final OllamaProperties properties;

    public OllamaProxyController(OllamaChatClient ollamaChatClient, OllamaProperties properties) {
        this.ollamaChatClient = ollamaChatClient;
        this.properties = properties;
    }

    @GetMapping("/tags")
    public ResponseEntity<JsonNode> listModels() {
        ensureEnabled();
        try {
            return ResponseEntity.ok(ollamaChatClient.listModels());
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Ollama indisponible: " + ex.getMessage());
        }
    }

    @PostMapping("/chat")
    public ResponseEntity<JsonNode> chat(@Valid @RequestBody OllamaChatProxyRequest request) {
        ensureEnabled();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", request.model());
        body.put("messages", request.messages().stream()
                .map(m -> Map.of("role", m.role(), "content", m.content()))
                .toList());
        body.put("stream", request.stream());
        try {
            JsonNode response = ollamaChatClient.rawChat(body);
            if (response == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Réponse Ollama vide");
            }
            return ResponseEntity.ok(response);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Ollama indisponible: " + ex.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("enabled", properties.isEnabled());
        body.put("model", properties.getModel());
        body.put("baseUrl", properties.getBaseUrl());
        return ResponseEntity.ok(body);
    }

    private void ensureEnabled() {
        if (!properties.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Ollama désactivé dans la configuration.");
        }
    }
}
