import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PfeTopic } from '../../core/models/pfe.models';
import { PfeService } from '../../core/services/pfe.service';

@Component({
  selector: 'app-pfe-application',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './pfe-application.component.html',
  styleUrl: './pfe-application.component.scss',
})
export class PfeApplicationComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly pfeService = inject(PfeService);
  private readonly route = inject(ActivatedRoute);

  readonly topics = signal<PfeTopic[]>([]);
  readonly submitMessage = signal<string | null>(null);
  readonly submitError = signal<string | null>(null);
  readonly submitting = signal(false);

  readonly applicationForm = this.fb.group({
    topicId: [null as number | null, Validators.required],
    fullName: ['', [Validators.required, Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
    school: ['', [Validators.required, Validators.maxLength(120)]],
    level: ['', [Validators.required, Validators.maxLength(80)]],
    motivation: ['', [Validators.required, Validators.minLength(50), Validators.maxLength(3000)]],
    portfolioUrl: ['', Validators.maxLength(500)],
  });

  readonly levelOptions = ['Licence', 'Master', 'Cycle ingénieur', 'Autre'];

  ngOnInit(): void {
    this.pfeService.listTopics(true).subscribe((topics) => {
      this.topics.set(topics);
      const queryTopicId = Number(this.route.snapshot.queryParamMap.get('topicId'));
      if (queryTopicId && topics.some((t) => t.id === queryTopicId)) {
        this.applicationForm.patchValue({ topicId: queryTopicId });
      }
    });
  }

  submitApplication(): void {
    this.submitMessage.set(null);
    this.submitError.set(null);

    if (this.applicationForm.invalid) {
      this.applicationForm.markAllAsTouched();
      return;
    }

    const value = this.applicationForm.getRawValue();
    this.submitting.set(true);

    this.pfeService
      .submitApplication({
        topicId: value.topicId!,
        fullName: value.fullName!,
        email: value.email!,
        school: value.school!,
        level: value.level!,
        motivation: value.motivation!,
        portfolioUrl: value.portfolioUrl || undefined,
      })
      .subscribe({
        next: (response) => {
          this.submitMessage.set(
            `Candidature enregistrée pour « ${response.topicTitle} ». Statut : ${response.status}.`
          );
          this.applicationForm.reset();
          this.submitting.set(false);
        },
        error: (err) => {
          const message = err?.error?.message ?? 'Impossible d\'envoyer la candidature pour le moment.';
          this.submitError.set(message);
          this.submitting.set(false);
        },
      });
  }

  motivationLength(): number {
    return this.applicationForm.get('motivation')?.value?.length ?? 0;
  }

  selectedTopic(): PfeTopic | null {
    const id = this.applicationForm.get('topicId')?.value;
    return this.topics().find((t) => t.id === id) ?? null;
  }

  invalid(controlName: string): boolean {
    const control = this.applicationForm.get(controlName);
    return !!(control && control.invalid && control.touched);
  }

  errorText(controlName: string): string {
    const control = this.applicationForm.get(controlName);
    if (!control || !control.errors) {
      return '';
    }
    if (control.errors['required']) {
      return 'Ce champ est obligatoire.';
    }
    if (control.errors['email']) {
      return 'Adresse e-mail invalide.';
    }
    if (control.errors['minlength']) {
      return `Minimum ${control.errors['minlength'].requiredLength} caractères.`;
    }
    if (control.errors['maxlength']) {
      return `Maximum ${control.errors['maxlength'].requiredLength} caractères.`;
    }
    return 'Valeur invalide.';
  }
}
