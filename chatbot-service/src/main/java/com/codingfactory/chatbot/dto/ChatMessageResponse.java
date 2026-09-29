package com.codingfactory.chatbot.dto;

import java.util.List;

public record ChatMessageResponse(
        String sessionId,
        String reply,
        String recommendedServiceCode,
        String recommendedServiceTitle,
        List<String> suggestedQuestions,
        boolean aiPowered,
        String assistantName,
        List<ConsultingServiceDto> suggestedServices
) {
}