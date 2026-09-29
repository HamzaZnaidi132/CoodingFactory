import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PfeCompletedProject } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';

@Component({
  selector: 'app-pfe-projects',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './pfe-projects.component.html',
  styleUrl: './pfe-projects.component.scss',
})
export class PfeProjectsComponent implements OnInit {
  private readonly pfeService = inject(PfeService);

  readonly projects = signal<PfeCompletedProject[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.pfeService.listProjects().subscribe({
      next: (projects) => {
        this.projects.set(projects);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les projets. Vérifiez que le service PFE est démarré.');
        this.loading.set(false);
      },
    });
  }
}
