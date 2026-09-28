package com.codingfactory.pfe.web;

import com.codingfactory.pfe.dto.*;
import com.codingfactory.pfe.service.PfeApplicationService;
import com.codingfactory.pfe.service.PfeCompletedProjectService;
import com.codingfactory.pfe.service.PfeRecommendationService;
import com.codingfactory.pfe.service.PfeTopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pfe")
public class PfeController {

    private final PfeTopicService topicService;
    private final PfeCompletedProjectService projectService;
    private final PfeApplicationService applicationService;
    private final PfeRecommendationService recommendationService;

    public PfeController(
            PfeTopicService topicService,
            PfeCompletedProjectService projectService,
            PfeApplicationService applicationService,
            PfeRecommendationService recommendationService
    ) {
        this.topicService = topicService;
        this.projectService = projectService;
        this.applicationService = applicationService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<PfeTopicDto>> listTopics(
            @RequestParam(defaultValue = "false") boolean openOnly
    ) {
        return ResponseEntity.ok(topicService.listTopics(openOnly));
    }

    @GetMapping("/topics/{id}")
    public ResponseEntity<PfeTopicDto> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopic(id));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<PfeCompletedProjectDto>> listProjects() {
        return ResponseEntity.ok(projectService.listProjects());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<PfeCompletedProjectDto> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @PostMapping("/applications")
    public ResponseEntity<PfeApplicationResponse> submitApplication(
            @Valid @RequestBody PfeApplicationRequest request
    ) {
        PfeApplicationResponse response = applicationService.submit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/recommendations")
    public ResponseEntity<List<PfeRecommendationResponse>> recommend(
            @Valid @RequestBody PfeRecommendationRequest request
    ) {
        return ResponseEntity.ok(recommendationService.recommend(request));
    }
}
