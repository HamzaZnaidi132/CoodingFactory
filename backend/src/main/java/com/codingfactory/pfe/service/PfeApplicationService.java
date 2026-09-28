package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeApplication;
import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeApplicationStatus;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import com.codingfactory.pfe.dto.PfeApplicationDetailResponse;
import com.codingfactory.pfe.dto.PfeApplicationRequest;
import com.codingfactory.pfe.dto.PfeApplicationResponse;
import com.codingfactory.pfe.repository.PfeApplicationRepository;
import com.codingfactory.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PfeApplicationService {

    private final PfeApplicationRepository applicationRepository;
    private final PfeTopicService topicService;

    public PfeApplicationService(PfeApplicationRepository applicationRepository, PfeTopicService topicService) {
        this.applicationRepository = applicationRepository;
        this.topicService = topicService;
    }

    @Transactional
    public PfeApplicationResponse submit(PfeApplicationRequest request) {
        PfeTopic topic = topicService.getEntity(request.topicId());

        if (topic.getStatus() != PfeTopicStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce sujet n'accepte plus de candidatures.");
        }

        long currentApplications = applicationRepository.countByTopicId(topic.getId());
        if (currentApplications >= topic.getMaxCandidates()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le nombre maximum de candidatures est atteint.");
        }

        if (applicationRepository.existsByTopicIdAndEmailIgnoreCase(topic.getId(), request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Une candidature existe déjà avec cet e-mail pour ce sujet.");
        }

        PfeApplication application = PfeApplication.builder()
                .topic(topic)
                .fullName(request.fullName())
                .email(request.email())
                .school(request.school())
                .level(request.level())
                .motivation(request.motivation())
                .portfolioUrl(request.portfolioUrl())
                .status(PfeApplicationStatus.RECEIVED)
                .submittedAt(Instant.now())
                .build();

        PfeApplication saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    public List<PfeApplicationDetailResponse> listAll() {
        return applicationRepository.findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(this::toDetailResponse)
                .toList();
    }

    public List<PfeApplicationDetailResponse> listByTopic(Long topicId) {
        return applicationRepository.findByTopicIdOrderBySubmittedAtDesc(topicId)
                .stream()
                .map(this::toDetailResponse)
                .toList();
    }

    @Transactional
    public PfeApplicationDetailResponse accept(Long id) {
        PfeApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable: " + id));
        application.setStatus(PfeApplicationStatus.ACCEPTED);
        return toDetailResponse(applicationRepository.save(application));
    }

    @Transactional
    public PfeApplicationDetailResponse reject(Long id) {
        PfeApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable: " + id));
        application.setStatus(PfeApplicationStatus.REJECTED);
        return toDetailResponse(applicationRepository.save(application));
    }

    private PfeApplicationResponse toResponse(PfeApplication application) {
        return new PfeApplicationResponse(
                application.getId(),
                application.getTopic().getId(),
                application.getTopic().getTitle(),
                application.getStatus(),
                application.getSubmittedAt()
        );
    }

    private PfeApplicationDetailResponse toDetailResponse(PfeApplication application) {
        return new PfeApplicationDetailResponse(
                application.getId(),
                application.getTopic().getId(),
                application.getTopic().getTitle(),
                application.getFullName(),
                application.getEmail(),
                application.getSchool(),
                application.getLevel(),
                application.getMotivation(),
                application.getPortfolioUrl(),
                application.getStatus(),
                application.getSubmittedAt()
        );
    }
}
