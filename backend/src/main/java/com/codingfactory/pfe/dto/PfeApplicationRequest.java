package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PfeApplicationRequest(
        @NotNull Long topicId,
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 120) String school,
        @NotBlank @Size(max = 80) String level,
        @NotBlank @Size(max = 3000) String motivation,
        @Size(max = 500) String portfolioUrl
) {
}
