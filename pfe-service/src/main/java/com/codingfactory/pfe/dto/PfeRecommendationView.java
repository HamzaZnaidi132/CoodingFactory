package com.codingfactory.pfe.dto;

public record PfeRecommendationView(
        Long topicId,
        String topicTitle,
        String domain,
        String technologies,
        String supervisorName,
        double score,
        java.util.List<String> matchReasons
) {
}