package com.codingfactory.chatbot.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatMessageRequest(
        @NotBlank String message,
        String sessionId,
        List<ConversationTurnDto> history
) {
}