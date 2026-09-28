package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import com.codingfactory.pfe.dto.PfeTopicCreateRequest;
import com.codingfactory.pfe.dto.PfeTopicDto;
import com.codingfactory.pfe.dto.PfeTopicUpdateRequest;
import com.codingfactory.pfe.repository.PfeApplicationRepository;
import com.codingfactory.pfe.repository.PfeTopicRepository;
import com.codingfactory.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PfeTopicService {

    private final PfeTopicRepository topicRepository;
    private final PfeApplicationRepository applicationRepository;

    public PfeTopicService(PfeTopicRepository topicRepository, PfeApplicationRepository applicationRepository) {
        this.topicRepository = topicRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<PfeTopicDto> listTopics(boolean openOnly) {
        List<PfeTopic> topics = openOnly
                ? topicRepository.findByStatusOrderByTitleAsc(PfeTopicStatus.OPEN)
                : topicRepository.findAllByOrderByTitleAsc();
        return topics.stream().map(this::toDto).toList();
    }

    public PfeTopicDto getTopic(Long id) {
        return topicRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable: " + id));
    }

    PfeTopic getEntity(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable: " + id));
    }

    @Transactional
    public PfeTopicDto createTopic(PfeTopicCreateRequest request) {
        PfeTopic topic = PfeTopic.builder()
                .title(request.title())
                .description(request.description())
                .domain(request.domain())
                .technologies(request.technologies())
                .supervisorName(request.supervisorName())
                .status(request.status())
                .maxCandidates(request.maxCandidates())
                .build();
        return toDto(topicRepository.save(topic));
    }

    @Transactional
    public PfeTopicDto updateTopic(Long id, PfeTopicUpdateRequest request) {
        PfeTopic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sujet PFE introuvable: " + id));

        if (request.title() != null) topic.setTitle(request.title());
        if (request.description() != null) topic.setDescription(request.description());
        if (request.domain() != null) topic.setDomain(request.domain());
        if (request.technologies() != null) topic.setTechnologies(request.technologies());
        if (request.supervisorName() != null) topic.setSupervisorName(request.supervisorName());
        if (request.status() != null) topic.setStatus(request.status());
        if (request.maxCandidates() != null) topic.setMaxCandidates(request.maxCandidates());

        return toDto(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sujet PFE introuvable: " + id);
        }
        topicRepository.deleteById(id);
    }

    private PfeTopicDto toDto(PfeTopic topic) {
        long count = applicationRepository.countByTopicId(topic.getId());
        return new PfeTopicDto(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getDomain(),
                topic.getTechnologies(),
                topic.getSupervisorName(),
                topic.getStatus(),
                topic.getMaxCandidates(),
                count
        );
    }
}
