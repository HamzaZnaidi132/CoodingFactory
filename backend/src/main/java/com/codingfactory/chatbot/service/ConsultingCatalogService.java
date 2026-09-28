package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.domain.ConsultingService;
import com.codingfactory.chatbot.dto.ConsultingServiceDto;
import com.codingfactory.chatbot.repository.ConsultingServiceRepository;
import com.codingfactory.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultingCatalogService {

    private final ConsultingServiceRepository repository;

    public ConsultingCatalogService(ConsultingServiceRepository repository) {
        this.repository = repository;
    }

    public List<ConsultingServiceDto> listAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public ConsultingServiceDto getByCode(String code) {
        return repository.findByCode(code)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Service consulting introuvable: " + code));
    }

    private ConsultingServiceDto toDto(ConsultingService entity) {
        return new ConsultingServiceDto(
                entity.getId(),
                entity.getCode(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getContactEmail()
        );
    }
}
