import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { PfeService } from './pfe.service';
import { environment } from '../../../environments/environment';

describe('PfeService', () => {
  let service: PfeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(PfeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should list topics', () => {
    service.listTopics(true).subscribe((topics) => {
      expect(topics.length).toBe(1);
    });

    const req = httpMock.expectOne(
      (request) => request.url === `${environment.apiUrl}/pfe/topics` && request.params.get('openOnly') === 'true'
    );
    req.flush([{ id: 1, title: 'Sujet test' }]);
  });
});
