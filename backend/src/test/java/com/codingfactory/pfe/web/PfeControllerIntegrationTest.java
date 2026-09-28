package com.codingfactory.pfe.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PfeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListOpenPfeTopics() throws Exception {
        mockMvc.perform(get("/api/v1/pfe/topics").param("openOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void shouldSubmitPfeApplication() throws Exception {
        String body = """
                {
                  "topicId": 1,
                  "fullName": "Test Student",
                  "email": "student.integration@test.com",
                  "school": "ESPRIT",
                  "level": "Master",
                  "motivation": "Je souhaite rejoindre CodingFactory pour ce PFE.",
                  "portfolioUrl": "https://github.com/student"
                }
                """;

        mockMvc.perform(post("/api/v1/pfe/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("RECEIVED"));
    }
}
