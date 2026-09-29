package com.codingfactory.pfe.domain;

import java.time.LocalDate;

public record PfeCompletedProject(
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