package com.codingfactory.chatbot.repository;

import com.codingfactory.chatbot.domain.ConsultingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultingServiceRepository extends JpaRepository<ConsultingService, Long> {

    Optional<ConsultingService> findByCode(String code);
}
