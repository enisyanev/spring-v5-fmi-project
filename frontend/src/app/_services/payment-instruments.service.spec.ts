import { TestBed } from '@angular/core/testing';

import { PaymentInstrumentsService } from './payment-instruments.service';

describe('PaymentInstrumentsService', () => {
  let service: PaymentInstrumentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentInstrumentsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
