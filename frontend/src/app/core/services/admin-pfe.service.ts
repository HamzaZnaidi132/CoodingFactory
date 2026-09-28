import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  PfeApplicationDetail,
  PfeCompletedProject,
  PfeProjectCreateRequest,
  PfeTopic,
  PfeTopicCreateRequest,
} from '../models/pfe.models';

@Injectable({ providedIn: 'root' })
export class AdminPfeService {
  private readonly baseUrl = `${environment.apiUrl}/admin/pfe`;

  constructor(private readonly http: HttpClient) {}

  // ────────── Topics ──────────

  createTopic(payload: PfeTopicCreateRequest): Observable<PfeTopic> {
    return this.http.post<PfeTopic>(`${this.baseUrl}/topics`, payload);
  }

  updateTopic(id: number, payload: Partial<PfeTopicCreateRequest>): Observable<PfeTopic> {
    return this.http.put<PfeTopic>(`${this.baseUrl}/topics/${id}`, payload);
  }

  deleteTopic(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/topics/${id}`);
  }

  // ────────── Projects ──────────

  createProject(payload: PfeProjectCreateRequest): Observable<PfeCompletedProject> {
    return this.http.post<PfeCompletedProject>(`${this.baseUrl}/projects`, payload);
  }

  updateProject(id: number, payload: Partial<PfeProjectCreateRequest>): Observable<PfeCompletedProject> {
    return this.http.put<PfeCompletedProject>(`${this.baseUrl}/projects/${id}`, payload);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/projects/${id}`);
  }

  // ────────── Applications ──────────

  listApplications(topicId?: number): Observable<PfeApplicationDetail[]> {
    let params = new HttpParams();
    if (topicId) {
      params = params.set('topicId', String(topicId));
    }
    return this.http.get<PfeApplicationDetail[]>(`${this.baseUrl}/applications`, { params });
  }

  acceptApplication(id: number): Observable<PfeApplicationDetail> {
    return this.http.put<PfeApplicationDetail>(`${this.baseUrl}/applications/${id}/accept`, {});
  }

  rejectApplication(id: number): Observable<PfeApplicationDetail> {
    return this.http.put<PfeApplicationDetail>(`${this.baseUrl}/applications/${id}/reject`, {});
  }
}
