package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import com.codingfactory.pfe.dto.PfeRecommendationRequest;
import com.codingfactory.pfe.dto.PfeRecommendationResponse;
import com.codingfactory.pfe.repository.PfeTopicRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service de recommandation ML pour les sujets PFE.
 *
 * Implémente un algorithme de scoring basé sur TF-IDF simplifié et
 * similarité cosinus pour recommander les sujets PFE les plus
 * pertinents selon le profil du candidat.
 */
@Service
public class PfeRecommendationService {

    private final PfeTopicRepository topicRepository;

    public PfeRecommendationService(PfeTopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /**
     * Recommande les top-N sujets PFE ouverts selon le profil candidat.
     */
    public List<PfeRecommendationResponse> recommend(PfeRecommendationRequest request) {
        List<PfeTopic> openTopics = topicRepository.findByStatusOrderByTitleAsc(PfeTopicStatus.OPEN);

        if (openTopics.isEmpty()) {
            return List.of();
        }

        // 1. Construire le vocabulaire global (IDF)
        Map<String, Double> idf = computeIdf(openTopics);

        // 2. Vectoriser le profil candidat
        String candidateText = normalize(request.skills() + " " + request.interests() + " " + request.level());
        Map<String, Double> candidateVector = tfidfVector(candidateText, idf);

        // 3. Scorer chaque sujet
        List<PfeRecommendationResponse> results = new ArrayList<>();
        for (PfeTopic topic : openTopics) {
            String topicText = normalize(
                    topic.getTitle() + " " + topic.getDescription() + " "
                    + topic.getDomain() + " " + topic.getTechnologies()
            );
            Map<String, Double> topicVector = tfidfVector(topicText, idf);
            double score = cosineSimilarity(candidateVector, topicVector);

            // Identifier les raisons de matching
            List<String> reasons = findMatchReasons(candidateText, topic);

            if (score > 0.0) {
                results.add(new PfeRecommendationResponse(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getDomain(),
                        topic.getTechnologies(),
                        topic.getSupervisorName(),
                        Math.round(score * 10000.0) / 100.0, // score en %
                        reasons
                ));
            }
        }

        // Trier par score décroissant, retourner top 5
        results.sort(Comparator.comparingDouble(PfeRecommendationResponse::score).reversed());
        return results.stream().limit(5).toList();
    }

    // ──────────── TF-IDF Engine ────────────

    /**
     * Calcule l'IDF (Inverse Document Frequency) pour chaque terme
     * à travers tous les sujets.
     */
    private Map<String, Double> computeIdf(List<PfeTopic> topics) {
        int n = topics.size();
        Map<String, Integer> docFrequency = new HashMap<>();

        for (PfeTopic topic : topics) {
            String text = normalize(
                    topic.getTitle() + " " + topic.getDescription() + " "
                    + topic.getDomain() + " " + topic.getTechnologies()
            );
            Set<String> uniqueTerms = new HashSet<>(tokenize(text));
            for (String term : uniqueTerms) {
                docFrequency.merge(term, 1, Integer::sum);
            }
        }

        Map<String, Double> idf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : docFrequency.entrySet()) {
            // IDF = log(N / df) + 1 (smoothed)
            idf.put(entry.getKey(), Math.log((double) n / entry.getValue()) + 1.0);
        }
        return idf;
    }

    /**
     * Construit le vecteur TF-IDF pour un texte donné.
     */
    private Map<String, Double> tfidfVector(String text, Map<String, Double> idf) {
        List<String> terms = tokenize(text);
        Map<String, Long> tf = terms.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        Map<String, Double> vector = new HashMap<>();
        long maxTf = tf.values().stream().max(Long::compare).orElse(1L);

        for (Map.Entry<String, Long> entry : tf.entrySet()) {
            double normalizedTf = 0.5 + 0.5 * ((double) entry.getValue() / maxTf);
            double idfVal = idf.getOrDefault(entry.getKey(), 1.0);
            vector.put(entry.getKey(), normalizedTf * idfVal);
        }
        return vector;
    }

    /**
     * Calcule la similarité cosinus entre deux vecteurs TF-IDF.
     */
    private double cosineSimilarity(Map<String, Double> a, Map<String, Double> b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        Set<String> allKeys = new HashSet<>(a.keySet());
        allKeys.addAll(b.keySet());

        for (String key : allKeys) {
            double valA = a.getOrDefault(key, 0.0);
            double valB = b.getOrDefault(key, 0.0);
            dotProduct += valA * valB;
            normA += valA * valA;
            normB += valB * valB;
        }

        if (normA == 0.0 || normB == 0.0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    // ──────────── Text Processing ────────────

    /**
     * Identifie les raisons concrètes de matching entre le profil et un sujet.
     */
    private List<String> findMatchReasons(String candidateText, PfeTopic topic) {
        List<String> reasons = new ArrayList<>();
        List<String> candidateTerms = tokenize(candidateText);
        Set<String> candidateSet = new HashSet<>(candidateTerms);

        // Match sur technologies
        for (String tech : tokenize(normalize(topic.getTechnologies()))) {
            if (candidateSet.contains(tech) && tech.length() > 2) {
                reasons.add("Compétence correspondante : " + tech);
            }
        }

        // Match sur domaine
        for (String domain : tokenize(normalize(topic.getDomain()))) {
            if (candidateSet.contains(domain) && domain.length() > 2) {
                reasons.add("Domaine correspondant : " + domain);
            }
        }

        // Match sur mots-clés du titre
        for (String titleWord : tokenize(normalize(topic.getTitle()))) {
            if (candidateSet.contains(titleWord) && titleWord.length() > 3) {
                reasons.add("Mot-clé du sujet : " + titleWord);
            }
        }

        if (reasons.isEmpty()) {
            reasons.add("Pertinence sémantique générale");
        }

        return reasons.stream().distinct().limit(5).toList();
    }

    /**
     * Normalise le texte : minuscules, suppression ponctuation, accents simplifiés.
     */
    private String normalize(String text) {
        return text.toLowerCase()
                .replaceAll("[éèêë]", "e")
                .replaceAll("[àâä]", "a")
                .replaceAll("[ùûü]", "u")
                .replaceAll("[îï]", "i")
                .replaceAll("[ôö]", "o")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * Tokenise le texte en termes individuels, filtre les stop words.
     */
    private List<String> tokenize(String text) {
        Set<String> stopWords = Set.of(
                "le", "la", "les", "de", "du", "des", "un", "une", "et", "en",
                "a", "au", "aux", "pour", "par", "sur", "avec", "dans", "est",
                "que", "qui", "ce", "se", "ne", "pas", "plus", "ou", "son",
                "the", "and", "for", "with", "from", "this", "that", "are",
                "is", "of", "to", "in", "on", "at", "an", "it", "be"
        );
        return Arrays.stream(text.split("\\s+"))
                .filter(t -> t.length() > 1 && !stopWords.contains(t))
                .toList();
    }
}
