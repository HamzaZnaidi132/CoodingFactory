package com.codingfactory.pfe.dto;

import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PfeTopicCreateRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 2000) String description,
        @NotBlank @Size(max = 80) String domain,
        @NotBlank @Size(max = 120) String technologies,
        @NotBlank @Size(max = 120) String supervisorName,
        @NotNull PfeTopicStatus status,
        @Min(1) int maxCandidates
) {
}
