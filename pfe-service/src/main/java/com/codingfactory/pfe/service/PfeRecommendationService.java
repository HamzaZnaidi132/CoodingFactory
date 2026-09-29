package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.dto.PfeRecommendationRequest;
import com.codingfactory.pfe.dto.PfeRecommendationView;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class PfeRecommendationService {

    private final PfeTopicService topicService;

    public PfeRecommendationService(PfeTopicService topicService) {
        this.topicService = topicService;
    }

    public List<PfeRecommendationView> recommend(PfeRecommendationRequest request) {
        List<String> terms = Stream.of(request.skills(), request.interests(), request.level())
            .flatMap(value -> Arrays.stream(value.toLowerCase(Locale.ROOT).split("[ ,;]+")))
            .toList();
        return topicService.findOpenTopics().stream()
                .map(topic -> toRecommendation(topic, terms))
                .sorted(Comparator.comparingDouble(PfeRecommendationView::score).reversed())
                .limit(3)
                .toList();
    }

    private PfeRecommendationView toRecommendation(PfeTopic topic, List<String> terms) {
        String haystack = (topic.title() + " " + topic.description() + " " + topic.domain() + " " + topic.technologies()).toLowerCase(Locale.ROOT);
        List<String> matched = terms.stream()
            .filter(term -> !term.isBlank() && haystack.contains(term))
            .distinct()
            .toList();
        double score = terms.isEmpty() ? 0.0 : Math.min(100.0, (matched.size() * 100.0) / Math.max(terms.size(), 1));
        List<String> reasons = matched.isEmpty()
            ? List.of("Sujet ouvert pertinent pour votre profil.")
            : matched.stream().map(term -> "Correspondance avec: " + term).toList();
        return new PfeRecommendationView(topic.id(), topic.title(), topic.domain(), topic.technologies(), topic.supervisorName(), score, reasons);
    }
}