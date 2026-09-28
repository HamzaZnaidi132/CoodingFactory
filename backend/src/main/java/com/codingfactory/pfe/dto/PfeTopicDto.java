package com.codingfactory.pfe.dto;

import com.codingfactory.pfe.domain.enums.PfeTopicStatus;

public record PfeTopicDto(
        Long id,
        String title,
        String description,
        String domain,
        String technologies,
        String supervisorName,
        PfeTopicStatus status,
        int maxCandidates,
        long applicationCount
) {
}
