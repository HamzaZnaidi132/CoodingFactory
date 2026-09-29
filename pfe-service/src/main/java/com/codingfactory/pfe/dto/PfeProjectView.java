package com.codingfactory.pfe.dto;

public record PfeProjectView(
        Long id,
        String title,
        String studentName,
        String academicYear,
        String summary,
        String methodology,
        String results,
        String completionDate
) {
}