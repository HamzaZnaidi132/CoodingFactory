import { Injectable, signal } from '@angular/core';

export type AppRole = 'candidat' | 'admin';

@Injectable({ providedIn: 'root' })
export class RoleService {
  readonly currentRole = signal<AppRole>('candidat');

  toggle(): void {
    this.currentRole.update((r) => (r === 'candidat' ? 'admin' : 'candidat'));
  }

  isAdmin(): boolean {
    return this.currentRole() === 'admin';
  }
}
