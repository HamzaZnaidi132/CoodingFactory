package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PfeApplicationRequest(
        @NotNull Long topicId,
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank String school,
        @NotBlank String level,
        @NotBlank String motivation,
        String portfolioUrl
) {
}