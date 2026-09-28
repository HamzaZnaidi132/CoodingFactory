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

  ngOnInit(): void {
    this.loadTopics();
  }

  toggleFilter(openOnly: boolean): void {
    this.showOpenOnly.set(openOnly);
    this.loadTopics();
  }

  private loadTopics(): void {
    this.pfeService.listTopics(this.showOpenOnly()).subscribe((topics) => this.topics.set(topics));
  }
}
