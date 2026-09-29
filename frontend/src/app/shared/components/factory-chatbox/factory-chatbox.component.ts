import { CommonModule } from '@angular/common';
import {
  AfterViewChecked,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  ViewChild,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatbotService } from '../../../core/services/chatbot.service';
import { ChatUiMessage, ConversationTurn } from '../../../core/models/chatbot.models';

@Component({
  selector: 'app-factory-chatbox',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './factory-chatbox.component.html',
  styleUrl: './factory-chatbox.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FactoryChatboxComponent implements AfterViewChecked {
  @ViewChild('messagesEl') messagesEl?: ElementRef<HTMLDivElement>;

  isOpen = false;
  messages: ChatUiMessage[] = [];
  inputText = '';
  loading = false;
  errorMessage = '';
  sessionId: string | null = null;
  aiModel = 'llama3.2';
  private scrollToBottom = false;

  constructor(
    private readonly chatbotService: ChatbotService,
    private readonly cdr: ChangeDetectorRef
  ) {
    this.chatbotService.assistantStatus().subscribe({
      next: (status) => {
        this.aiModel = status.model;
        this.cdr.markForCheck();
      },
      error: () => {},
    });
  }

  toggle(): void {
    this.isOpen = !this.isOpen;
    this.errorMessage = '';
    if (this.isOpen) {
      this.scrollToBottom = true;
      if (this.messages.length === 0) {
        this.messages.push({
          id: 'welcome',
          role: 'assistant',
          content:
            'Bonjour, je suis FactoryBot. Posez vos questions sur le consulting, les formations ou les PFE CodingFactory.',
        });
      }
    }
    this.cdr.markForCheck();
  }

  close(): void {
    this.isOpen = false;
    this.cdr.markForCheck();
  }

  clear(): void {
    this.messages = [];
    this.sessionId = null;
    this.errorMessage = '';
    this.cdr.markForCheck();
  }

  ngAfterViewChecked(): void {
    if (this.scrollToBottom && this.messagesEl?.nativeElement) {
      const el = this.messagesEl.nativeElement;
      el.scrollTop = el.scrollHeight;
      this.scrollToBottom = false;
    }
  }

  send(): void {
    const text = this.inputText.trim();
    if (!text || this.loading) {
      return;
    }

    this.messages.push({ id: `u-${Date.now()}`, role: 'user', content: text });
    this.inputText = '';
    this.loading = true;
    this.errorMessage = '';
    this.scrollToBottom = true;
    this.cdr.markForCheck();

    const history: ConversationTurn[] = this.messages
      .filter((m) => m.id !== 'welcome')
      .slice(0, -1)
      .map((m) => ({ role: m.role, content: m.content }));

    this.chatbotService.sendMessage({ message: text, sessionId: this.sessionId, history }).subscribe({
      next: (res) => {
        this.sessionId = res.sessionId;
        this.messages.push({
          id: `a-${Date.now()}`,
          role: 'assistant',
          content: res.reply,
          serviceTitle: res.recommendedServiceTitle,
          aiPowered: res.aiPowered,
        });
        this.loading = false;
        this.scrollToBottom = true;
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage =
          'Assistant indisponible. Vérifiez le gateway (8090) et Ollama (ollama serve + ollama pull llama3.2).';
        this.loading = false;
        this.cdr.markForCheck();
      },
    });
  }
}
