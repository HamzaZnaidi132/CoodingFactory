import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PfeTopic, PfeTopicStatus } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';
import { AdminPfeService } from '../../core/services/admin-pfe.service';

@Component({
  selector: 'app-admin-topics',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-topics.component.html',
  styleUrl: './admin-topics.component.scss',
})
export class AdminTopicsComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly pfeService = inject(PfeService);
  private readonly adminService = inject(AdminPfeService);

  readonly topics = signal<PfeTopic[]>([]);
  readonly loading = signal(true);
  readonly showForm = signal(false);
  readonly editing = signal<PfeTopic | null>(null);
  readonly message = signal<string | null>(null);
  readonly error = signal<string | null>(null);

  readonly statusOptions: PfeTopicStatus[] = ['OPEN', 'CLOSED', 'ASSIGNED'];

  readonly topicForm = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(150)]],
    description: ['', [Validators.required, Validators.maxLength(2000)]],
    domain: ['', [Validators.required, Validators.maxLength(80)]],
    technologies: ['', [Validators.required, Validators.maxLength(120)]],
    supervisorName: ['', [Validators.required, Validators.maxLength(120)]],
    status: ['OPEN' as PfeTopicStatus, Validators.required],
    maxCandidates: [3, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    this.loadTopics();
  }

  loadTopics(): void {
    this.loading.set(true);
    this.pfeService.listTopics().subscribe({
      next: (data) => {
        this.topics.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  openCreateForm(): void {
    this.editing.set(null);
    this.topicForm.reset({ status: 'OPEN', maxCandidates: 3 });
    this.showForm.set(true);
    this.clearMessages();
  }

  openEditForm(topic: PfeTopic): void {
    this.editing.set(topic);
    this.topicForm.patchValue({
      title: topic.title,
      description: topic.description,
      domain: topic.domain,
      technologies: topic.technologies,
      supervisorName: topic.supervisorName,
      status: topic.status,
      maxCandidates: topic.maxCandidates,
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
    if (this.topicForm.invalid) {
      this.topicForm.markAllAsTouched();
      return;
    }

    const value = this.topicForm.getRawValue();
    const payload = {
      title: value.title!,
      description: value.description!,
      domain: value.domain!,
      technologies: value.technologies!,
      supervisorName: value.supervisorName!,
      status: value.status!,
      maxCandidates: value.maxCandidates!,
    };

    const editingTopic = this.editing();
    if (editingTopic) {
      this.adminService.updateTopic(editingTopic.id, payload).subscribe({
        next: () => {
          this.message.set('Sujet mis à jour avec succès.');
          this.showForm.set(false);
          this.loadTopics();
        },
        error: (err) => this.error.set(err?.error?.message ?? 'Erreur lors de la mise à jour.'),
      });
    } else {
      this.adminService.createTopic(payload).subscribe({
        next: () => {
          this.message.set('Sujet créé avec succès.');
          this.showForm.set(false);
          this.loadTopics();
        },
        error: (err) => this.error.set(err?.error?.message ?? 'Erreur lors de la création.'),
      });
    }
  }

  deleteTopic(topic: PfeTopic): void {
    if (!confirm(`Supprimer le sujet « ${topic.title} » ?`)) return;
    this.adminService.deleteTopic(topic.id).subscribe({
      next: () => {
        this.message.set('Sujet supprimé.');
        this.loadTopics();
      },
      error: (err) => this.error.set(err?.error?.message ?? 'Erreur lors de la suppression.'),
    });
  }

  statusLabel(status: string): string {
    const labels: Record<string, string> = {
      OPEN: 'Ouvert',
      CLOSED: 'Fermé',
      ASSIGNED: 'Assigné',
    };
    return labels[status] || status;
  }

  invalid(name: string): boolean {
    const c = this.topicForm.get(name);
    return !!(c && c.invalid && c.touched);
  }

  private clearMessages(): void {
    this.message.set(null);
    this.error.set(null);
  }
}
