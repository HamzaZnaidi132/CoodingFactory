package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PfeRecommendationRequest(
        @NotBlank @Size(max = 500) String skills,
        @NotBlank @Size(max = 500) String interests,
        @NotBlank @Size(max = 80) String level
) {
}
