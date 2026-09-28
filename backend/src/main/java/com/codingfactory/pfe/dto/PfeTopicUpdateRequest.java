package com.codingfactory.pfe.dto;

import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import jakarta.validation.constraints.Size;

public record PfeTopicUpdateRequest(
        @Size(max = 150) String title,
        @Size(max = 2000) String description,
        @Size(max = 80) String domain,
        @Size(max = 120) String technologies,
        @Size(max = 120) String supervisorName,
        PfeTopicStatus status,
        Integer maxCandidates
) {
}
