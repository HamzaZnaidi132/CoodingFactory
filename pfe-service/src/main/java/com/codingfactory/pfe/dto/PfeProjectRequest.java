package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.NotBlank;

public record PfeProjectRequest(
        @NotBlank String title,
        @NotBlank String studentName,
        @NotBlank String academicYear,
        @NotBlank String summary,
        @NotBlank String methodology,
        @NotBlank String results,
        @NotBlank String completionDate
) {
}