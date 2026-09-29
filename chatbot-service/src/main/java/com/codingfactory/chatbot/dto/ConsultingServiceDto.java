package com.codingfactory.chatbot.dto;

public record ConsultingServiceDto(
        String code,
        String title,
        String description,
        String contactEmail,
        String keywords
) {
}