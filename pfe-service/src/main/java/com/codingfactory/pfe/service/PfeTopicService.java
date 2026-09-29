package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import com.codingfactory.pfe.dto.PfeTopicRequest;
import com.codingfactory.pfe.dto.PfeTopicView;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PfeTopicService {

    private final Map<Long, PfeTopic> topics = new LinkedHashMap<>();
    private final Map<Long, Integer> applicationCounts = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public PfeTopicService() {
        seed();
    }

    public List<PfeTopicView> listTopics(boolean openOnly) {
        return topics.values().stream()
                .filter(topic -> !openOnly || topic.status() == PfeTopicStatus.OPEN)
                .map(this::toView)
                .toList();
    }

    public PfeTopicView getTopic(Long id) {
        return toView(findTopic(id));
    }

    public List<PfeTopic> findOpenTopics() {
        return new ArrayList<>(topics.values().stream()
                .filter(topic -> topic.status() == PfeTopicStatus.OPEN)
                .toList());
    }

    public PfeTopic create(PfeTopicRequest request) {
        Long id = sequence.incrementAndGet();
        PfeTopic topic = new PfeTopic(id, request.title(), request.description(), request.domain(), request.technologies(), request.supervisorName(), request.status(), request.maxCandidates());
        topics.put(id, topic);
        applicationCounts.putIfAbsent(id, 0);
        return topic;
    }

    public PfeTopicView update(Long id, PfeTopicRequest request) {
        findTopic(id);
        PfeTopic topic = new PfeTopic(id, request.title(), request.description(), request.domain(), request.technologies(), request.supervisorName(), request.status(), request.maxCandidates());
        topics.put(id, topic);
        return toView(topic);
    }

    public void delete(Long id) {
        if (topics.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
        applicationCounts.remove(id);
    }

    public PfeTopic requireTopic(Long id) {
        return findTopic(id);
    }

    public void incrementApplicationCount(Long topicId) {
        if (!topics.containsKey(topicId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
        applicationCounts.put(topicId, applicationCounts.getOrDefault(topicId, 0) + 1);
    }

    private PfeTopic findTopic(Long id) {
        PfeTopic topic = topics.get(id);
        if (topic == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
        return topic;
    }

    private PfeTopicView toView(PfeTopic topic) {
        return new PfeTopicView(topic.id(), topic.title(), topic.description(), topic.domain(), topic.technologies(), topic.supervisorName(), topic.status(), topic.maxCandidates(), applicationCounts.getOrDefault(topic.id(), 0));
    }

    private void seed() {
        create(new PfeTopicRequest(
                "Plateforme e-learning adaptative",
                "Conception d'une plateforme d'apprentissage modulaire qui ajuste les parcours selon le niveau et les résultats.",
                "Education",
                "Spring Boot, Angular, PostgreSQL",
                "Dr. Amira Ben Salah",
                PfeTopicStatus.OPEN,
                3));
        create(new PfeTopicRequest(
                "Assistant conversationnel IA & RAG",
                "Chatbot intelligent pour orienter les besoins clients à partir des offres CodingFactory et d'une base documentaire.",
                "IA & NLP",
                "Spring Boot, Python, Ollama",
                "Ing. Karim Trabelsi",
                PfeTopicStatus.OPEN,
                2));
        create(new PfeTopicRequest(
                "Observabilité Cloud-Native et GitOps",
                "Pipeline GitOps automatisé multi-environnements avec métriques, traces et alertes.",
                "DevOps & Cloud",
                "Kubernetes, ArgoCD, Prometheus",
                "Ing. Youssef Mabrouk",
                PfeTopicStatus.ASSIGNED,
                1));
        create(new PfeTopicRequest(
                "Marketplace B2B artisans 2.0",
                "Évolution de la place de marché : paiements, logistique et scoring qualité fournisseurs.",
                "E-commerce",
                "Spring Boot, Angular, Stripe, PostgreSQL",
                "Dr. Amira Ben Salah",
                PfeTopicStatus.OPEN,
                2));
        create(new PfeTopicRequest(
                "SOC lite pour PME",
                "Prototype de centre d'opérations de sécurité : collecte de logs, corrélation et playbooks de réponse.",
                "Cybersécurité",
                "Spring Boot, Elastic, Angular",
                "Ing. Karim Trabelsi",
                PfeTopicStatus.OPEN,
                2));
        create(new PfeTopicRequest(
                "Jumeau numérique d'un atelier",
                "Modélisation d'un atelier de production pour simuler les goulots d'étranglement et planifier la maintenance.",
                "Industrie 4.0",
                "Python, Spring Boot, Angular, IoT",
                "Ing. Youssef Mabrouk",
                PfeTopicStatus.OPEN,
                2));
    }
}