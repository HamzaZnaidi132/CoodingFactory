import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PfeTopic, PfeApplicationDetail, PfeCompletedProject } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';
import { AdminPfeService } from '../../core/services/admin-pfe.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss',
})
export class AdminDashboardComponent implements OnInit {
  private readonly pfeService = inject(PfeService);
  private readonly adminService = inject(AdminPfeService);

  readonly topics = signal<PfeTopic[]>([]);
  readonly projects = signal<PfeCompletedProject[]>([]);
  readonly applications = signal<PfeApplicationDetail[]>([]);
  readonly loading = signal(true);

  ngOnInit(): void {
    forkJoin({
      topics: this.pfeService.listTopics(),
      projects: this.pfeService.listProjects(),
      applications: this.adminService.listApplications(),
    }).subscribe({
      next: (data) => {
        this.topics.set(data.topics);
        this.projects.set(data.projects);
        this.applications.set(data.applications);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  get pendingCount(): number {
    return this.applications().filter((a) => a.status === 'RECEIVED' || a.status === 'UNDER_REVIEW').length;
  }

  get acceptedCount(): number {
    return this.applications().filter((a) => a.status === 'ACCEPTED').length;
  }

  get rejectedCount(): number {
    return this.applications().filter((a) => a.status === 'REJECTED').length;
  }

  get openTopicsCount(): number {
    return this.topics().filter((t) => t.status === 'OPEN').length;
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
}
