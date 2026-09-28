package com.codingfactory.pfe.dto;

import com.codingfactory.pfe.domain.enums.PfeApplicationStatus;

import java.time.Instant;

public record PfeApplicationResponse(
        Long id,
        Long topicId,
        String topicTitle,
        PfeApplicationStatus status,
        Instant submittedAt
) {
}
