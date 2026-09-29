package com.codingfactory.chatbot.web;

import com.codingfactory.chatbot.service.ConsultingChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ollama")
public class OllamaStatusController {

    private final ConsultingChatbotService chatbotService;

    public OllamaStatusController(ConsultingChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(chatbotService.getAssistantStatus());
    }
}