import { TestBed } from '@angular/core/testing';

import { TfaServiceService } from './tfa-service.service';

describe('TfaServiceService', () => {
  let service: TfaServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TfaServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
