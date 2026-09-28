import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { PfeApplicationDetail } from '../../core/models/pfe.models';
import { AdminPfeService } from '../../core/services/admin-pfe.service';

@Component({
  selector: 'app-admin-applications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-applications.component.html',
  styleUrl: './admin-applications.component.scss',
})
export class AdminApplicationsComponent implements OnInit {
  private readonly adminService = inject(AdminPfeService);

  readonly applications = signal<PfeApplicationDetail[]>([]);
  readonly loading = signal(true);
  readonly message = signal<string | null>(null);
  readonly error = signal<string | null>(null);
  readonly expandedId = signal<number | null>(null);
  readonly filter = signal<string>('ALL');

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.loading.set(true);
    this.adminService.listApplications().subscribe({
      next: (data) => {
        this.applications.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  accept(app: PfeApplicationDetail): void {
    this.adminService.acceptApplication(app.id).subscribe({
      next: (updated) => {
        this.message.set(`Candidature de ${updated.fullName} acceptée.`);
        this.error.set(null);
        this.updateInList(updated);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'Erreur lors de l\'acceptation.');
        this.message.set(null);
      },
    });
  }

  reject(app: PfeApplicationDetail): void {
    if (!confirm(`Refuser la candidature de ${app.fullName} ?`)) return;
    this.adminService.rejectApplication(app.id).subscribe({
      next: (updated) => {
        this.message.set(`Candidature de ${updated.fullName} refusée.`);
        this.error.set(null);
        this.updateInList(updated);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'Erreur lors du refus.');
        this.message.set(null);
      },
    });
  }

  toggleExpand(id: number): void {
    this.expandedId.update((current) => (current === id ? null : id));
  }

  setFilter(status: string): void {
    this.filter.set(status);
  }

  get filteredApplications(): PfeApplicationDetail[] {
    const f = this.filter();
    if (f === 'ALL') return this.applications();
    return this.applications().filter((a) => a.status === f);
  }

  statusLabel(status: string): string {
    const labels: Record<string, string> = {
      RECEIVED: 'Reçue',
      UNDER_REVIEW: 'En cours',
      ACCEPTED: 'Acceptée',
      REJECTED: 'Refusée',
    };
    return labels[status] || status;
  }

  private updateInList(updated: PfeApplicationDetail): void {
    this.applications.update((list) =>
      list.map((a) => (a.id === updated.id ? updated : a))
    );
  }
}
