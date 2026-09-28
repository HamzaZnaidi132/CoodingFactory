package com.codingfactory.pfe.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PfeProjectUpdateRequest(
        @Size(max = 150) String title,
        @Size(max = 120) String studentName,
        @Size(max = 120) String academicYear,
        @Size(max = 2000) String summary,
        @Size(max = 2000) String methodology,
        @Size(max = 2000) String results,
        LocalDate completionDate
) {
}
