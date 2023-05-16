import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmsValidationComponent } from './sms-validation.component';

describe('SmsValidationComponent', () => {
  let component: SmsValidationComponent;
  let fixture: ComponentFixture<SmsValidationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmsValidationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SmsValidationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
