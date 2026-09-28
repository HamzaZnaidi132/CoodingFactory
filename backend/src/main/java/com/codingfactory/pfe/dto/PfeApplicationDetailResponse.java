package com.codingfactory.pfe.dto;

import com.codingfactory.pfe.domain.enums.PfeApplicationStatus;

import java.time.Instant;

public record PfeApplicationDetailResponse(
        Long id,
        Long topicId,
        String topicTitle,
        String fullName,
        String email,
        String school,
        String level,
        String motivation,
        String portfolioUrl,
        PfeApplicationStatus status,
        Instant submittedAt
) {
}
