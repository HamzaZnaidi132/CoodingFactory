import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PfeCompletedProject } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';
import { AdminPfeService } from '../../core/services/admin-pfe.service';

@Component({
  selector: 'app-admin-projects',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-projects.component.html',
  styleUrl: './admin-projects.component.scss',
})
export class AdminProjectsComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly pfeService = inject(PfeService);
  private readonly adminService = inject(AdminPfeService);

  readonly projects = signal<PfeCompletedProject[]>([]);
  readonly loading = signal(true);
  readonly showForm = signal(false);
  readonly editing = signal<PfeCompletedProject | null>(null);
  readonly message = signal<string | null>(null);
  readonly error = signal<string | null>(null);

  readonly projectForm = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(150)]],
    studentName: ['', [Validators.required, Validators.maxLength(120)]],
    academicYear: ['', [Validators.required, Validators.maxLength(120)]],
    summary: ['', [Validators.required, Validators.maxLength(2000)]],
    methodology: ['', [Validators.required, Validators.maxLength(2000)]],
    results: ['', [Validators.required, Validators.maxLength(2000)]],
    completionDate: ['', Validators.required],
  });

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.loading.set(true);
    this.pfeService.listProjects().subscribe({
      next: (data) => {
        this.projects.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  openCreateForm(): void {
    this.editing.set(null);
    this.projectForm.reset();
    this.showForm.set(true);
    this.clearMessages();
  }

  openEditForm(project: PfeCompletedProject): void {
    this.editing.set(project);
    this.projectForm.patchValue({
      title: project.title,
      studentName: project.studentName,
      academicYear: project.academicYear,
      summary: project.summary,
      methodology: project.methodology,
      results: project.results,
      completionDate: project.completionDate,
    });
    this.showForm.set(true);
    this.clearMessages();
  }

  cancelForm(): void {
    this.showForm.set(false);
    this.editing.set(null);
    this.clearMessages();
  }

  submitForm(): void {
    if (this.projectForm.invalid) {
      this.projectForm.markAllAsTouched();
      return;
    }

    const value = this.projectForm.getRawValue();
    const payload = {
      title: value.title!,
      studentName: value.studentName!,
      academicYear: value.academicYear!,
      summary: value.summary!,
      methodology: value.methodology!,
      results: value.results!,
      completionDate: value.completionDate!,
    };

    const editingProject = this.editing();
    if (editingProject) {
      this.adminService.updateProject(editingProject.id, payload).subscribe({
        next: () => {
          this.message.set('Projet mis à jour avec succès.');
          this.showForm.set(false);
          this.loadProjects();
        },
        error: (err) => this.error.set(err?.error?.message ?? 'Erreur lors de la mise à jour.'),
      });
    } else {
      this.adminService.createProject(payload).subscribe({
        next: () => {
          this.message.set('Projet créé avec succès.');
          this.showForm.set(false);
          this.loadProjects();
        },
        error: (err) => this.error.set(err?.error?.message ?? 'Erreur lors de la création.'),
      });
    }
  }

  deleteProject(project: PfeCompletedProject): void {
    if (!confirm(`Supprimer le projet « ${project.title} » ?`)) return;
    this.adminService.deleteProject(project.id).subscribe({
      next: () => {
        this.message.set('Projet supprimé.');
        this.loadProjects();
      },
      error: (err) => this.error.set(err?.error?.message ?? 'Erreur lors de la suppression.'),
    });
  }

  invalid(name: string): boolean {
    const c = this.projectForm.get(name);
    return !!(c && c.invalid && c.touched);
  }

  private clearMessages(): void {
    this.message.set(null);
    this.error.set(null);
  }
}
