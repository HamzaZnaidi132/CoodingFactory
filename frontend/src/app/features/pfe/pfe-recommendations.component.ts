import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PfeRecommendation } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';

@Component({
  selector: 'app-pfe-recommendations',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './pfe-recommendations.component.html',
  styleUrl: './pfe-recommendations.component.scss',
})
export class PfeRecommendationsComponent {
  private readonly fb = inject(FormBuilder);
  private readonly pfeService = inject(PfeService);

  readonly recommendations = signal<PfeRecommendation[]>([]);
  readonly loading = signal(false);
  readonly searched = signal(false);

  readonly levelOptions = ['Licence', 'Master', 'Cycle ingénieur', 'Autre'];

  readonly profileForm = this.fb.group({
    skills: ['', [Validators.required, Validators.maxLength(500)]],
    interests: ['', [Validators.required, Validators.maxLength(500)]],
    level: ['', Validators.required],
  });

  search(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const value = this.profileForm.getRawValue();
    this.loading.set(true);
    this.searched.set(false);

    this.pfeService
      .getRecommendations({
        skills: value.skills!,
        interests: value.interests!,
        level: value.level!,
      })
      .subscribe({
        next: (data) => {
          this.recommendations.set(data);
          this.loading.set(false);
          this.searched.set(true);
        },
        error: () => {
          this.loading.set(false);
          this.searched.set(true);
        },
      });
  }

  invalid(name: string): boolean {
    const c = this.profileForm.get(name);
    return !!(c && c.invalid && c.touched);
  }

  scoreColor(score: number): string {
    if (score >= 60) return '#059669';
    if (score >= 30) return '#d97706';
    return '#9ca3af';
  }
}
