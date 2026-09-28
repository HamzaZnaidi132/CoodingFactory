import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PfeService } from '../../core/services/pfe.service';

@Component({
  selector: 'app-pfe-portal',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './pfe-portal.component.html',
  styleUrl: './pfe-portal.component.scss',
})
export class PfePortalComponent implements OnInit {
  private readonly pfeService = inject(PfeService);

  readonly openTopics = signal(0);
  readonly projectsCount = signal(0);

  ngOnInit(): void {
    this.pfeService.listTopics(true).subscribe((topics) => this.openTopics.set(topics.length));
    this.pfeService.listProjects().subscribe((projects) => this.projectsCount.set(projects.length));
  }
}
