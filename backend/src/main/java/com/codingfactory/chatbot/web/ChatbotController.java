package com.codingfactory.chatbot.web;

import com.codingfactory.chatbot.dto.ChatMessageRequest;
import com.codingfactory.chatbot.dto.ChatMessageResponse;
import com.codingfactory.chatbot.dto.ConsultingServiceDto;
import com.codingfactory.chatbot.service.ConsultingCatalogService;
import com.codingfactory.chatbot.service.ConsultingChatbotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatbotController {

    private final ConsultingChatbotService chatbotService;
    private final ConsultingCatalogService catalogService;

    public ChatbotController(ConsultingChatbotService chatbotService, ConsultingCatalogService catalogService) {
        this.chatbotService = chatbotService;
        this.catalogService = catalogService;
    }

    @PostMapping("/consulting/message")
    public ResponseEntity<ChatMessageResponse> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        ChatMessageResponse response = chatbotService.handleMessage(
                request.message(),
                request.sessionId(),
                request.history()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consulting/services")
    public ResponseEntity<List<ConsultingServiceDto>> listServices() {
        return ResponseEntity.ok(catalogService.listAll());
    }

    @GetMapping("/consulting/services/{code}")
    public ResponseEntity<ConsultingServiceDto> getService(@PathVariable String code) {
        return ResponseEntity.ok(catalogService.getByCode(code));
    }
}
