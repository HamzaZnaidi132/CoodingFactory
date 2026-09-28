import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ChatMessageRequest,
  ChatMessageResponse,
  ConsultingService,
} from '../models/chatbot.models';

@Injectable({ providedIn: 'root' })
export class ChatbotService {
  private readonly baseUrl = `${environment.apiUrl}/chatbot/consulting`;

  constructor(private readonly http: HttpClient) {}

  sendMessage(payload: ChatMessageRequest): Observable<ChatMessageResponse> {
    return this.http.post<ChatMessageResponse>(`${this.baseUrl}/message`, payload);
  }

  listServices(): Observable<ConsultingService[]> {
    return this.http.get<ConsultingService[]>(`${this.baseUrl}/services`);
  }

  assistantStatus(): Observable<{ enabled: boolean; model: string; baseUrl: string }> {
    return this.http.get<{ enabled: boolean; model: string; baseUrl: string }>(
      `${environment.apiUrl}/ollama/status`
    );
  }
}
