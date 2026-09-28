import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'pfeStatusLabel', standalone: true })
export class PfeStatusLabelPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    switch (value) {
      case 'OPEN':
        return 'Ouvert';
      case 'CLOSED':
        return 'Fermé';
      case 'ASSIGNED':
        return 'Attribué';
      default:
        return value ?? '';
    }
  }
}
