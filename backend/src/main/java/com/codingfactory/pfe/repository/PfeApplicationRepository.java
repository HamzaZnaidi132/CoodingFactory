package com.codingfactory.pfe.repository;

import com.codingfactory.pfe.domain.PfeApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PfeApplicationRepository extends JpaRepository<PfeApplication, Long> {

    long countByTopicId(Long topicId);

    boolean existsByTopicIdAndEmailIgnoreCase(Long topicId, String email);

    @EntityGraph(attributePaths = {"topic"})
    List<PfeApplication> findAllByOrderBySubmittedAtDesc();

    @EntityGraph(attributePaths = {"topic"})
    List<PfeApplication> findByTopicIdOrderBySubmittedAtDesc(Long topicId);

    @Override
    @EntityGraph(attributePaths = {"topic"})
    Optional<PfeApplication> findById(Long id);
}

