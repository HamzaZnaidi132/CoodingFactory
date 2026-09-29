package com.codingfactory.pfe.domain;

import com.codingfactory.pfe.domain.enums.PfeTopicStatus;

public record PfeTopic(
        Long id,
        String title,
        String description,
        String domain,
        String technologies,
        String supervisorName,
        PfeTopicStatus status,
        int maxCandidates
) {
}