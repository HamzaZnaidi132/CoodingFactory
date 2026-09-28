import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  PfeApplicationRequest,
  PfeApplicationResponse,
  PfeCompletedProject,
  PfeRecommendation,
  PfeRecommendationRequest,
  PfeTopic,
} from '../models/pfe.models';

@Injectable({ providedIn: 'root' })
export class PfeService {
  private readonly baseUrl = `${environment.apiUrl}/pfe`;

  constructor(private readonly http: HttpClient) {}

  listTopics(openOnly = false): Observable<PfeTopic[]> {
    const params = new HttpParams().set('openOnly', String(openOnly));
    return this.http.get<PfeTopic[]>(`${this.baseUrl}/topics`, { params });
  }

  getTopic(id: number): Observable<PfeTopic> {
    return this.http.get<PfeTopic>(`${this.baseUrl}/topics/${id}`);
  }

  listProjects(): Observable<PfeCompletedProject[]> {
    return this.http.get<PfeCompletedProject[]>(`${this.baseUrl}/projects`);
  }

  getProject(id: number): Observable<PfeCompletedProject> {
    return this.http.get<PfeCompletedProject>(`${this.baseUrl}/projects/${id}`);
  }

  submitApplication(payload: PfeApplicationRequest): Observable<PfeApplicationResponse> {
    return this.http.post<PfeApplicationResponse>(`${this.baseUrl}/applications`, payload);
  }

  getRecommendations(payload: PfeRecommendationRequest): Observable<PfeRecommendation[]> {
    return this.http.post<PfeRecommendation[]>(`${this.baseUrl}/recommendations`, payload);
  }
}
