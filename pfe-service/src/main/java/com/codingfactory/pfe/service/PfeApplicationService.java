package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeApplication;
import com.codingfactory.pfe.domain.enums.PfeApplicationStatus;
import com.codingfactory.pfe.dto.PfeApplicationRequest;
import com.codingfactory.pfe.dto.PfeApplicationView;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PfeApplicationService {

    private final Map<Long, PfeApplication> applications = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    private final PfeTopicService topicService;

    public PfeApplicationService(PfeTopicService topicService) {
        this.topicService = topicService;
        seed();
    }

    public List<PfeApplicationView> listApplications(Long topicId) {
        return applications.values().stream()
                .filter(application -> topicId == null || application.topicId().equals(topicId))
                .map(this::toView)
                .toList();
    }

    public PfeApplicationView submit(PfeApplicationRequest request) {
        var topic = topicService.requireTopic(request.topicId());
        Long id = sequence.incrementAndGet();
        PfeApplication application = new PfeApplication(id, topic.id(), request.fullName(), request.email(), request.school(), request.level(), request.motivation(), request.portfolioUrl(), PfeApplicationStatus.RECEIVED, Instant.now());
        applications.put(id, application);
        topicService.incrementApplicationCount(topic.id());
        return toView(application);
    }

    public PfeApplicationView accept(Long id) {
        return updateStatus(id, PfeApplicationStatus.ACCEPTED);
    }

    public PfeApplicationView reject(Long id) {
        return updateStatus(id, PfeApplicationStatus.REJECTED);
    }

    private PfeApplicationView updateStatus(Long id, PfeApplicationStatus status) {
        PfeApplication current = findApplication(id);
        PfeApplication updated = new PfeApplication(current.id(), current.topicId(), current.fullName(), current.email(), current.school(), current.level(), current.motivation(), current.portfolioUrl(), status, current.submittedAt());
        applications.put(id, updated);
        return toView(updated);
    }

    private PfeApplication findApplication(Long id) {
        PfeApplication application = applications.get(id);
        if (application == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        return application;
    }

    private PfeApplicationView toView(PfeApplication application) {
        String topicTitle = topicService.requireTopic(application.topicId()).title();
        return new PfeApplicationView(application.id(), application.topicId(), topicTitle, application.fullName(), application.email(), application.school(), application.level(), application.motivation(), application.portfolioUrl(), application.status(), application.submittedAt());
    }

    private void seed() {
        submit(new PfeApplicationRequest(1L, "Amine Khelifi", "amine.khelifi@esprit.tn", "ESPRIT", "Cycle ingénieur", "Passionné par le développement web full-stack et l'UX pédagogique.", "https://github.com/amine-khelifi"));
        submit(new PfeApplicationRequest(2L, "Rania Mansouri", "rania.mansouri@tek-up.tn", "TEK-UP University", "Master", "Mon mémoire porte sur les plateformes adaptatives et le RAG.", null));
        submit(new PfeApplicationRequest(4L, "Hedi Bouzid", "hedi.bouzid@insat.tn", "INSAT", "Cycle ingénieur", "Expérience e-commerce et envie de travailler les paiements et la logistique.", "https://github.com/hedi-bouzid"));
        submit(new PfeApplicationRequest(5L, "Safa Jebali", "safa.jebali@supcom.tn", "SUP'COM", "Master", "Intéressée par la détection d'anomalies et la réponse à incident.", null));
    }
}