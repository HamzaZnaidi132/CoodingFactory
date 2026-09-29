package com.codingfactory.pfe.web;

import com.codingfactory.pfe.dto.PfeApplicationView;
import com.codingfactory.pfe.dto.PfeProjectRequest;
import com.codingfactory.pfe.dto.PfeProjectView;
import com.codingfactory.pfe.dto.PfeTopicRequest;
import com.codingfactory.pfe.dto.PfeTopicView;
import com.codingfactory.pfe.service.PfeApplicationService;
import com.codingfactory.pfe.service.PfeCompletedProjectService;
import com.codingfactory.pfe.service.PfeTopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/pfe")
public class AdminPfeController {

    private final PfeTopicService topicService;
    private final PfeCompletedProjectService projectService;
    private final PfeApplicationService applicationService;

    public AdminPfeController(PfeTopicService topicService, PfeCompletedProjectService projectService, PfeApplicationService applicationService) {
        this.topicService = topicService;
        this.projectService = projectService;
        this.applicationService = applicationService;
    }

    @PostMapping("/topics")
    public ResponseEntity<PfeTopicView> createTopic(@Valid @RequestBody PfeTopicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.getTopic(topicService.create(request).id()));
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<PfeTopicView> updateTopic(@PathVariable Long id, @Valid @RequestBody PfeTopicRequest request) {
        return ResponseEntity.ok(topicService.update(id, request));
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/projects")
    public ResponseEntity<PfeProjectView> createProject(@Valid @RequestBody PfeProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.getProject(projectService.create(request).id()));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<PfeProjectView> updateProject(@PathVariable Long id, @Valid @RequestBody PfeProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applications")
    public ResponseEntity<List<PfeApplicationView>> listApplications(@RequestParam(required = false) Long topicId) {
        return ResponseEntity.ok(applicationService.listApplications(topicId));
    }

    @PutMapping("/applications/{id}/accept")
    public ResponseEntity<PfeApplicationView> accept(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.accept(id));
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<PfeApplicationView> reject(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.reject(id));
    }
}