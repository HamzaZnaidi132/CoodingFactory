package com.codingfactory.pfe.domain;

import com.codingfactory.pfe.domain.enums.PfeApplicationStatus;

import java.time.Instant;

public record PfeApplication(
        Long id,
        Long topicId,
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