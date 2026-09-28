import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatbotService } from '../../core/services/chatbot.service';
import { ChatMessageResponse, ConsultingService } from '../../core/models/chatbot.models';

interface ChatBubble {
  author: 'user' | 'bot';
  text: string;
  serviceTitle?: string | null;
}

@Component({
  selector: 'app-consulting-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulting-chatbot.component.html',
  styleUrl: './consulting-chatbot.component.scss',
})
export class ConsultingChatbotComponent implements OnInit {
  readonly messages = signal<ChatBubble[]>([]);
  readonly services = signal<ConsultingService[]>([]);
  readonly suggestions = signal<string[]>([]);
  readonly loading = signal(false);

  sessionId: string | null = null;
  draft = '';

  constructor(private readonly chatbotService: ChatbotService) {}

  ngOnInit(): void {
    this.chatbotService.listServices().subscribe((services) => this.services.set(services));
    this.pushBotMessage(
      "Bonjour, décrivez votre besoin consulting (cloud, data, sécurité, transformation...) et je vous orienterai."
    );
  }

  sendMessage(text?: string): void {
    const message = (text ?? this.draft).trim();
    if (!message || this.loading()) {
      return;
    }

    this.messages.update((items) => [...items, { author: 'user', text: message }]);
    this.draft = '';
    this.loading.set(true);

    this.chatbotService
      .sendMessage({ message, sessionId: this.sessionId })
      .subscribe({
        next: (response) => this.handleResponse(response),
        error: () => {
          this.pushBotMessage("Désolé, le service est momentanément indisponible. Réessayez dans quelques instants.");
          this.loading.set(false);
        },
      });
  }

  useSuggestion(question: string): void {
    this.sendMessage(question);
  }

  private handleResponse(response: ChatMessageResponse): void {
    this.sessionId = response.sessionId;
    this.suggestions.set(response.suggestedQuestions ?? []);
    this.pushBotMessage(response.reply, response.recommendedServiceTitle);
    this.loading.set(false);
  }

  private pushBotMessage(text: string, serviceTitle?: string | null): void {
    this.messages.update((items) => [...items, { author: 'bot', text, serviceTitle }]);
  }
}
