package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.domain.ChatSession;
import com.codingfactory.chatbot.domain.ConsultingService;
import com.codingfactory.chatbot.dto.ChatMessageResponse;
import com.codingfactory.chatbot.dto.ConversationTurnDto;
import com.codingfactory.chatbot.repository.ChatSessionRepository;
import com.codingfactory.chatbot.repository.ConsultingServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsultingChatbotService {

    private static final String ASSISTANT_NAME = "FactoryBot";
    private static final List<String> GREETINGS = List.of("bonjour", "salut", "hello", "bonsoir", "coucou");
    private static final List<String> DEFAULT_SUGGESTIONS = List.of(
            "Nous avons besoin d'un audit cloud",
            "Formation équipe sur le DevOps",
            "Projet data / IA pour notre entreprise",
            "Accompagnement transformation digitale"
    );

    private final ConsultingServiceRepository consultingServiceRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final OllamaChatClient ollamaChatClient;

    public ConsultingChatbotService(
            ConsultingServiceRepository consultingServiceRepository,
            ChatSessionRepository chatSessionRepository,
            OllamaChatClient ollamaChatClient
    ) {
        this.consultingServiceRepository = consultingServiceRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.ollamaChatClient = ollamaChatClient;
    }

    @Transactional
    public ChatMessageResponse handleMessage(String rawMessage, String sessionId, List<ConversationTurnDto> history) {
        String message = normalize(rawMessage);
        ChatSession session = resolveSession(sessionId);
        List<ConversationTurnDto> safeHistory = history == null ? List.of() : history;

        Optional<ConsultingService> match = findBestMatch(message);
        match.ifPresent(service -> {
            session.setLastRecommendedServiceCode(service.getCode());
            session.setUpdatedAt(Instant.now());
            chatSessionRepository.save(session);
        });

        Optional<String> aiReply = ollamaChatClient.chat(
                buildSystemPrompt(),
                trimHistory(safeHistory),
                rawMessage.trim()
        );

        if (aiReply.isPresent()) {
            return enrichWithRecommendation(
                    session.getSessionId(),
                    aiReply.get(),
                    match.orElse(null),
                    true
            );
        }

        return buildLocalResponse(session, message, match.orElse(null));
    }

    private ChatMessageResponse buildLocalResponse(ChatSession session, String message, ConsultingService matched) {
        if (isGreeting(message)) {
            return enrichWithRecommendation(
                    session.getSessionId(),
                    """
                            Bonjour, je suis FactoryBot, l'assistant consulting de CodingFactory.
                            Décrivez votre besoin (cloud, data, sécurité, transformation, formation) et je vous orienterai.
                            """.trim(),
                    null,
                    false
            );
        }

        if (matched != null) {
            String reply = """
                    D'après votre besoin, je vous oriente vers « %s ».
                    %s
                    Contact : %s — ou planifiez un rendez-vous via le formulaire consulting.
                    """.formatted(matched.getTitle(), matched.getDescription(), matched.getContactEmail()).trim();
            return enrichWithRecommendation(session.getSessionId(), reply, matched, false);
        }

        String fallback = """
                Je peux vous orienter vers : cloud & DevOps, data & IA, cybersécurité, transformation digitale ou formation sur mesure.
                Précisez votre secteur, la taille de l'équipe et votre objectif.
                """;
        return enrichWithRecommendation(session.getSessionId(), fallback, null, false);
    }

    private ChatMessageResponse enrichWithRecommendation(
            String sessionId,
            String reply,
            ConsultingService matched,
            boolean aiPowered
    ) {
        return new ChatMessageResponse(
                sessionId,
                reply,
                matched != null ? matched.getCode() : null,
                matched != null ? matched.getTitle() : null,
                DEFAULT_SUGGESTIONS,
                aiPowered,
                ASSISTANT_NAME
        );
    }

    private String buildSystemPrompt() {
        String catalog = consultingServiceRepository.findAll().stream()
                .map(s -> "- %s (%s) : %s. Contact: %s".formatted(
                        s.getTitle(), s.getCode(), s.getDescription(), s.getContactEmail()))
                .collect(Collectors.joining("\n"));

        return """
                Tu es FactoryBot, l'assistant IA officiel de CodingFactory (consulting et formation IT en Tunisie).
                Tu réponds en français, de façon professionnelle, claire et concise (3 à 8 phrases sauf demande contraire).

                Rôle :
                - Répondre aux questions sur les services de consulting CodingFactory
                - Aider à choisir l'offre adaptée (cloud, DevOps, data, IA, cybersécurité, transformation digitale, formation)
                - Expliquer comment prendre contact ou planifier une consultation
                - Mentionner les formations et le PFE uniquement si l'utilisateur le demande

                Règles :
                - Reste dans le périmètre CodingFactory (consulting, formation, PFE). Hors sujet : redirige poliment.
                - Si tu recommandes un service, cite son titre exact depuis le catalogue.
                - Ne invente pas de prix ni de dates ; propose de contacter l'équipe commerciale.

                Catalogue des offres :
                %s
                """.formatted(catalog);
    }

    private List<ConversationTurnDto> trimHistory(List<ConversationTurnDto> history) {
        int maxTurns = 12;
        if (history.size() <= maxTurns) {
            return history;
        }
        return history.subList(history.size() - maxTurns, history.size());
    }

    private ChatSession resolveSession(String sessionId) {
        Instant now = Instant.now();
        if (sessionId != null && !sessionId.isBlank()) {
            return chatSessionRepository.findBySessionId(sessionId)
                    .map(existing -> {
                        existing.setUpdatedAt(now);
                        return chatSessionRepository.save(existing);
                    })
                    .orElseGet(() -> chatSessionRepository.save(ChatSession.builder()
                            .sessionId(sessionId)
                            .createdAt(now)
                            .updatedAt(now)
                            .build()));
        }
        String newId = UUID.randomUUID().toString();
        return chatSessionRepository.save(ChatSession.builder()
                .sessionId(newId)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    private Optional<ConsultingService> findBestMatch(String message) {
        return consultingServiceRepository.findAll().stream()
                .map(service -> new ScoredService(service, score(message, service)))
                .filter(scored -> scored.score() > 0)
                .max(Comparator.comparingInt(ScoredService::score))
                .map(ScoredService::service);
    }

    private int score(String message, ConsultingService service) {
        int score = 0;
        score += keywordHits(message, service.getKeywords());
        score += keywordHits(message, service.getTitle());
        score += keywordHits(message, service.getDescription());
        return score;
    }

    private int keywordHits(String message, String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        int hits = 0;
        for (String token : text.toLowerCase(Locale.ROOT).split("[,;\\s]+")) {
            if (token.length() >= 3 && message.contains(token)) {
                hits++;
            }
        }
        return hits;
    }

    private boolean isGreeting(String message) {
        return GREETINGS.stream().anyMatch(message::contains);
    }

    private String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        return Arrays.stream(raw.toLowerCase(Locale.ROOT).trim().split("\\s+"))
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }

    private record ScoredService(ConsultingService service, int score) {
    }
}
