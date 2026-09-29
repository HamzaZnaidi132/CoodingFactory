import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PfeTopic } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';
import { PfeStatusLabelPipe } from '../../shared/pipes/pfe-status-label.pipe';

@Component({
  selector: 'app-pfe-topics',
  standalone: true,
  imports: [RouterLink, PfeStatusLabelPipe],
  templateUrl: './pfe-topics.component.html',
  styleUrl: './pfe-topics.component.scss',
})
export class PfeTopicsComponent implements OnInit {
  private readonly pfeService = inject(PfeService);

  readonly topics = signal<PfeTopic[]>([]);
  readonly showOpenOnly = signal(true);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadTopics();
  }

  toggleFilter(openOnly: boolean): void {
    this.showOpenOnly.set(openOnly);
    this.loadTopics();
  }

  private loadTopics(): void {
    this.loading.set(true);
    this.error.set(null);
    this.pfeService.listTopics(this.showOpenOnly()).subscribe({
      next: (topics) => {
        this.topics.set(topics);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les sujets. Vérifiez que le service PFE est démarré.');
        this.loading.set(false);
      },
    });
  }
}
