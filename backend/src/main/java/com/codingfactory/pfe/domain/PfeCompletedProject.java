package com.codingfactory.pfe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pfe_completed_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PfeCompletedProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 120)
    private String studentName;

    @Column(nullable = false, length = 120)
    private String academicYear;

    @Column(nullable = false, length = 2000)
    private String summary;

    @Column(nullable = false, length = 2000)
    private String methodology;

    @Column(nullable = false, length = 2000)
    private String results;

    @Column(nullable = false)
    private LocalDate completionDate;
}
