export interface ConversationTurn {
  role: 'user' | 'assistant';
  content: string;
}

export interface ChatMessageRequest {
  message: string;
  sessionId?: string | null;
  history?: ConversationTurn[];
}

export interface ChatMessageResponse {
  sessionId: string;
  reply: string;
  recommendedServiceCode?: string | null;
  recommendedServiceTitle?: string | null;
  suggestedQuestions: string[];
  aiPowered: boolean;
  assistantName: string;
}

export interface ConsultingService {
  id: number;
  code: string;
  title: string;
  description: string;
  contactEmail: string;
}

export interface ChatUiMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  serviceTitle?: string | null;
  aiPowered?: boolean;
}
