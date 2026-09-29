package com.codingfactory.pfe.dto;

import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PfeTopicRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String domain,
        @NotBlank String technologies,
        @NotBlank String supervisorName,
        @NotNull PfeTopicStatus status,
        int maxCandidates
) {
}