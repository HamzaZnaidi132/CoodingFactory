package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.dto.ChatMessageRequest;
import com.codingfactory.chatbot.dto.ChatMessageResponse;
import com.codingfactory.chatbot.dto.ConsultingServiceDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class ConsultingChatbotService {

    private final List<ConsultingServiceDto> services = List.of(
            new ConsultingServiceDto("cloud-devops", "Cloud & DevOps", "Audit cloud, migration, CI/CD, conteneurisation et observabilité.", "cloud@codingfactory.tn", "cloud, devops, kubernetes, docker, ci/cd"),
            new ConsultingServiceDto("data-ai", "Data & IA", "Architecture data, pipelines, ML ops et cas d'usage IA métier.", "data@codingfactory.tn", "data, ia, ai, machine learning, analytics"),
            new ConsultingServiceDto("cybersecurity", "Cybersécurité", "Audit sécurité, durcissement, conformité et sensibilisation.", "security@codingfactory.tn", "sécurité, cyber, audit, conformité"),
            new ConsultingServiceDto("digital-transformation", "Transformation digitale", "Accompagnement stratégique et modernisation SI.", "consulting@codingfactory.tn", "transformation, digital, modernisation, stratégie"),
            new ConsultingServiceDto("custom-training", "Formation sur mesure", "Parcours adaptés aux équipes : développement, agile, cloud, data.", "formation@codingfactory.tn", "formation, training, équipe, upskilling")
    );

    public ChatMessageResponse handleMessage(ChatMessageRequest request) {
        String sessionId = request.sessionId() == null || request.sessionId().isBlank()
                ? UUID.randomUUID().toString()
                : request.sessionId();
        String message = request.message().toLowerCase(Locale.ROOT);
        List<ConsultingServiceDto> matches = services.stream()
                .filter(service -> matches(service, message))
                .toList();

        List<ConsultingServiceDto> suggested = matches.isEmpty() ? services.subList(0, 2) : matches;
        ConsultingServiceDto primary = suggested.get(0);
        String reply = matches.isEmpty()
                ? "Je peux vous orienter vers les offres Cloud, Data, Cybersécurité, Transformation digitale ou Formation. Précisez votre besoin pour affiner."
                : "Voici les services les plus proches de votre besoin : " + primary.title() + ". " + primary.description();

        return new ChatMessageResponse(
            sessionId,
            reply,
            primary.code(),
            primary.title(),
            buildSuggestedQuestions(primary),
            true,
            "CodingFactory Assistant",
            suggested
        );
    }

    public List<ConsultingServiceDto> listServices() {
        return services;
    }

    public ConsultingServiceDto getService(String code) {
        return services.stream()
                .filter(service -> service.code().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
    }

    public Map<String, Object> getAssistantStatus() {
        return Map.of(
                "enabled", Boolean.parseBoolean(System.getenv().getOrDefault("OLLAMA_ENABLED", "true")),
                "model", System.getenv().getOrDefault("OLLAMA_MODEL", "llama3.2"),
                "baseUrl", System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434")
        );
    }

    private boolean matches(ConsultingServiceDto service, String message) {
        return message.contains(service.code()) || Arrays.stream(service.keywords().split(","))
                .map(String::trim)
                .anyMatch(message::contains);
    }

    private List<String> buildSuggestedQuestions(ConsultingServiceDto service) {
        return List.of(
                "Pouvez-vous préciser votre besoin sur " + service.title() + " ?",
                "Quel est votre budget ou horizon de déploiement ?",
                "Souhaitez-vous une proposition de cadrage ou d'audit ?"
        );
    }
}