package com.codingfactory.chatbot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChatMessageRequest(
        @NotBlank @Size(max = 2000) String message,
        String sessionId,
        List<@Valid ConversationTurnDto> history
) {
    public ChatMessageRequest {
        if (history == null) {
            history = List.of();
        }
    }
}
