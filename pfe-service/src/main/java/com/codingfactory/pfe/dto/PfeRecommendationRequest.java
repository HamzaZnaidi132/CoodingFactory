package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.NotBlank;

public record PfeRecommendationRequest(
        @NotBlank String skills,
        @NotBlank String interests,
        @NotBlank String level
) {
}