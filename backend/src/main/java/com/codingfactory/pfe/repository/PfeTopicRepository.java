package com.codingfactory.pfe.repository;

import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfeTopicRepository extends JpaRepository<PfeTopic, Long> {

    List<PfeTopic> findByStatusOrderByTitleAsc(PfeTopicStatus status);

    List<PfeTopic> findAllByOrderByTitleAsc();
}
