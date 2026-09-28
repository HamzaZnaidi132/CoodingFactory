package com.codingfactory.pfe.domain;

import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pfe_topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PfeTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 80)
    private String domain;

    @Column(nullable = false, length = 120)
    private String technologies;

    @Column(nullable = false, length = 120)
    private String supervisorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PfeTopicStatus status;

    @Column(nullable = false)
    private int maxCandidates;
}
