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

  ngOnInit(): void {
    this.pfeService.listProjects().subscribe((projects) => this.projects.set(projects));
  }
}
