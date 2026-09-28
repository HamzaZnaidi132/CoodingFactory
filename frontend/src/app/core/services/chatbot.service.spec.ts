import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ChatbotService } from './chatbot.service';
import { environment } from '../../../environments/environment';

describe('ChatbotService', () => {
  let service: ChatbotService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(ChatbotService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should send chat message', () => {
    service.sendMessage({ message: 'Bonjour' }).subscribe((response) => {
      expect(response.reply).toContain('assistant');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/chatbot/consulting/message`);
    expect(req.request.method).toBe('POST');
    req.flush({
      sessionId: 'abc',
      reply: 'Bonjour assistant',
      suggestedQuestions: [],
    });
  });
});
