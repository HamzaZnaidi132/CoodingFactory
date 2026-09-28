package com.codingfactory.pfe.repository;

import com.codingfactory.pfe.domain.PfeCompletedProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfeCompletedProjectRepository extends JpaRepository<PfeCompletedProject, Long> {

    List<PfeCompletedProject> findAllByOrderByCompletionDateDesc();
}
