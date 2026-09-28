package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PfeProjectCreateRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 120) String studentName,
        @NotBlank @Size(max = 120) String academicYear,
        @NotBlank @Size(max = 2000) String summary,
        @NotBlank @Size(max = 2000) String methodology,
        @NotBlank @Size(max = 2000) String results,
        @NotNull LocalDate completionDate
) {
}
