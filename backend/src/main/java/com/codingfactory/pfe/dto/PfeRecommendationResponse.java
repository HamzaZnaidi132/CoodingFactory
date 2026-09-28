package com.codingfactory.pfe.dto;

import java.util.List;

public record PfeRecommendationResponse(
        Long topicId,
        String topicTitle,
        String domain,
        String technologies,
        String supervisorName,
        double score,
        List<String> matchReasons
) {
}
