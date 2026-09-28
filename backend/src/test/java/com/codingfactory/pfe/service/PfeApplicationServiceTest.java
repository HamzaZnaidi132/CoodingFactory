package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import com.codingfactory.pfe.dto.PfeApplicationRequest;
import com.codingfactory.pfe.repository.PfeApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfeApplicationServiceTest {

    @Mock
    private PfeApplicationRepository applicationRepository;

    @Mock
    private PfeTopicService topicService;

    @InjectMocks
    private PfeApplicationService applicationService;

    @Test
    void shouldRejectApplicationWhenTopicIsClosed() {
        PfeTopic topic = PfeTopic.builder()
                .id(1L)
                .title("Sujet fermé")
                .status(PfeTopicStatus.CLOSED)
                .maxCandidates(2)
                .build();

        when(topicService.getEntity(1L)).thenReturn(topic);

        PfeApplicationRequest request = new PfeApplicationRequest(
                1L, "Ali Ben", "ali@test.com", "ESPRIT", "Ingénieur", "Motivation", null
        );

        assertThatThrownBy(() -> applicationService.submit(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("n'accepte plus");
    }
}
