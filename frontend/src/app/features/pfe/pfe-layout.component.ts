import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-pfe-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './pfe-layout.component.html',
  styleUrl: './pfe-layout.component.scss',
})
export class PfeLayoutComponent {}
