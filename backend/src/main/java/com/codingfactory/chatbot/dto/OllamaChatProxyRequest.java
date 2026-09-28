package com.codingfactory.chatbot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OllamaChatProxyRequest(
        @NotBlank String model,
        @NotEmpty List<@Valid ConversationTurnDto> messages,
        boolean stream
) {
}
