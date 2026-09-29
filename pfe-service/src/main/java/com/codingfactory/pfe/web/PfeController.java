package com.codingfactory.pfe.web;

import com.codingfactory.pfe.dto.PfeApplicationRequest;
import com.codingfactory.pfe.dto.PfeApplicationView;
import com.codingfactory.pfe.dto.PfeProjectView;
import com.codingfactory.pfe.dto.PfeRecommendationRequest;
import com.codingfactory.pfe.dto.PfeRecommendationView;
import com.codingfactory.pfe.dto.PfeTopicView;
import com.codingfactory.pfe.service.PfeApplicationService;
import com.codingfactory.pfe.service.PfeCompletedProjectService;
import com.codingfactory.pfe.service.PfeRecommendationService;
import com.codingfactory.pfe.service.PfeTopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pfe")
public class PfeController {

    private final PfeTopicService topicService;
    private final PfeCompletedProjectService projectService;
    private final PfeApplicationService applicationService;
    private final PfeRecommendationService recommendationService;

    public PfeController(PfeTopicService topicService, PfeCompletedProjectService projectService, PfeApplicationService applicationService, PfeRecommendationService recommendationService) {
        this.topicService = topicService;
        this.projectService = projectService;
        this.applicationService = applicationService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<PfeTopicView>> listTopics(@RequestParam(defaultValue = "false") boolean openOnly) {
        return ResponseEntity.ok(topicService.listTopics(openOnly));
    }

    @GetMapping("/topics/{id}")
    public ResponseEntity<PfeTopicView> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopic(id));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<PfeProjectView>> listProjects() {
        return ResponseEntity.ok(projectService.listProjects());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<PfeProjectView> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @PostMapping("/applications")
    public ResponseEntity<PfeApplicationView> submitApplication(@Valid @RequestBody PfeApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.submit(request));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<List<PfeRecommendationView>> recommend(@Valid @RequestBody PfeRecommendationRequest request) {
        return ResponseEntity.ok(recommendationService.recommend(request));
    }
}