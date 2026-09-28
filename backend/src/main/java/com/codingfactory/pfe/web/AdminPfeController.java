package com.codingfactory.pfe.web;

import com.codingfactory.pfe.dto.*;
import com.codingfactory.pfe.service.PfeApplicationService;
import com.codingfactory.pfe.service.PfeCompletedProjectService;
import com.codingfactory.pfe.service.PfeTopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/pfe")
public class AdminPfeController {

    private final PfeTopicService topicService;
    private final PfeCompletedProjectService projectService;
    private final PfeApplicationService applicationService;

    public AdminPfeController(
            PfeTopicService topicService,
            PfeCompletedProjectService projectService,
            PfeApplicationService applicationService
    ) {
        this.topicService = topicService;
        this.projectService = projectService;
        this.applicationService = applicationService;
    }

    // ────────── Sujets PFE (Topics) ──────────

    @PostMapping("/topics")
    public ResponseEntity<PfeTopicDto> createTopic(@Valid @RequestBody PfeTopicCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createTopic(request));
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<PfeTopicDto> updateTopic(@PathVariable Long id, @Valid @RequestBody PfeTopicUpdateRequest request) {
        return ResponseEntity.ok(topicService.updateTopic(id, request));
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    // ────────── Projets réalisés (Completed Projects) ──────────

    @PostMapping("/projects")
    public ResponseEntity<PfeCompletedProjectDto> createProject(@Valid @RequestBody PfeProjectCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<PfeCompletedProjectDto> updateProject(@PathVariable Long id, @Valid @RequestBody PfeProjectUpdateRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ────────── Candidatures ──────────

    @GetMapping("/applications")
    public ResponseEntity<List<PfeApplicationDetailResponse>> listApplications(
            @RequestParam(required = false) Long topicId
    ) {
        List<PfeApplicationDetailResponse> results = (topicId != null)
                ? applicationService.listByTopic(topicId)
                : applicationService.listAll();
        return ResponseEntity.ok(results);
    }

    @PutMapping("/applications/{id}/accept")
    public ResponseEntity<PfeApplicationDetailResponse> acceptApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.accept(id));
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<PfeApplicationDetailResponse> rejectApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.reject(id));
    }
}
