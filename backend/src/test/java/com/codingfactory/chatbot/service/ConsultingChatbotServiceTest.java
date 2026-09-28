package com.codingfactory.chatbot.service;

import com.codingfactory.chatbot.domain.ConsultingService;
import com.codingfactory.chatbot.dto.ChatMessageResponse;
import com.codingfactory.chatbot.repository.ChatSessionRepository;
import com.codingfactory.chatbot.repository.ConsultingServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultingChatbotServiceTest {

    @Mock
    private ConsultingServiceRepository consultingServiceRepository;

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private OllamaChatClient ollamaChatClient;

    @InjectMocks
    private ConsultingChatbotService chatbotService;

    @Test
    void shouldRecommendCloudDevOpsWhenMessageMentionsKubernetes() {
        when(consultingServiceRepository.findAll()).thenReturn(List.of(
                ConsultingService.builder()
                        .code("cloud-devops")
                        .title("Cloud & DevOps")
                        .description("CI/CD et cloud")
                        .contactEmail("cloud@codingfactory.tn")
                        .keywords("cloud,devops,kubernetes")
                        .build()
        ));
        when(chatSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ChatMessageResponse response = chatbotService.handleMessage(
                "Nous cherchons de l'aide kubernetes et devops",
                null,
                List.of()
        );

        assertThat(response.recommendedServiceCode()).isEqualTo("cloud-devops");
        assertThat(response.reply()).contains("Cloud & DevOps");
        assertThat(response.sessionId()).isNotBlank();
    }

    @Test
    void shouldReturnGreetingForHelloMessage() {
        when(chatSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ChatMessageResponse response = chatbotService.handleMessage("Bonjour", null, List.of());

        assertThat(response.recommendedServiceCode()).isNull();
        assertThat(response.reply()).contains("assistant consulting");
    }
}
