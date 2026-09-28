package com.codingfactory.chatbot.dto;

public record ConsultingServiceDto(
        Long id,
        String code,
        String title,
        String description,
        String contactEmail
) {
}
