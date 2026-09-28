package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeCompletedProject;
import com.codingfactory.pfe.dto.PfeCompletedProjectDto;
import com.codingfactory.pfe.dto.PfeProjectCreateRequest;
import com.codingfactory.pfe.dto.PfeProjectUpdateRequest;
import com.codingfactory.pfe.repository.PfeCompletedProjectRepository;
import com.codingfactory.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PfeCompletedProjectService {

    private final PfeCompletedProjectRepository repository;

    public PfeCompletedProjectService(PfeCompletedProjectRepository repository) {
        this.repository = repository;
    }

    public List<PfeCompletedProjectDto> listProjects() {
        return repository.findAllByOrderByCompletionDateDesc().stream().map(this::toDto).toList();
    }

    public PfeCompletedProjectDto getProject(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Projet PFE réalisé introuvable: " + id));
    }

    @Transactional
    public PfeCompletedProjectDto createProject(PfeProjectCreateRequest request) {
        PfeCompletedProject project = PfeCompletedProject.builder()
                .title(request.title())
                .studentName(request.studentName())
                .academicYear(request.academicYear())
                .summary(request.summary())
                .methodology(request.methodology())
                .results(request.results())
                .completionDate(request.completionDate())
                .build();
        return toDto(repository.save(project));
    }

    @Transactional
    public PfeCompletedProjectDto updateProject(Long id, PfeProjectUpdateRequest request) {
        PfeCompletedProject project = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet PFE réalisé introuvable: " + id));

        if (request.title() != null) project.setTitle(request.title());
        if (request.studentName() != null) project.setStudentName(request.studentName());
        if (request.academicYear() != null) project.setAcademicYear(request.academicYear());
        if (request.summary() != null) project.setSummary(request.summary());
        if (request.methodology() != null) project.setMethodology(request.methodology());
        if (request.results() != null) project.setResults(request.results());
        if (request.completionDate() != null) project.setCompletionDate(request.completionDate());

        return toDto(repository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Projet PFE réalisé introuvable: " + id);
        }
        repository.deleteById(id);
    }

    private PfeCompletedProjectDto toDto(PfeCompletedProject project) {
        return new PfeCompletedProjectDto(
                project.getId(),
                project.getTitle(),
                project.getStudentName(),
                project.getAcademicYear(),
                project.getSummary(),
                project.getMethodology(),
                project.getResults(),
                project.getCompletionDate()
        );
    }
}
