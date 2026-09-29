package com.codingfactory.chatbot.dto;

public record ConversationTurnDto(
        String role,
        String content
) {
}