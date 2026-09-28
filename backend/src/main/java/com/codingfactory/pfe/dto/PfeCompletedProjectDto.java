package com.codingfactory.pfe.dto;

import java.time.LocalDate;

public record PfeCompletedProjectDto(
        Long id,
        String title,
        String studentName,
        String academicYear,
        String summary,
        String methodology,
        String results,
        LocalDate completionDate
) {
}
