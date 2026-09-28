import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PfeCompletedProject } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';

@Component({
  selector: 'app-pfe-project-detail',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './pfe-project-detail.component.html',
  styleUrl: './pfe-project-detail.component.scss',
})
export class PfeProjectDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly pfeService = inject(PfeService);

  readonly project = signal<PfeCompletedProject | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.pfeService.getProject(id).subscribe({
      next: (project) => {
        this.project.set(project);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Projet introuvable ou indisponible.');
        this.loading.set(false);
      },
    });
  }
}
