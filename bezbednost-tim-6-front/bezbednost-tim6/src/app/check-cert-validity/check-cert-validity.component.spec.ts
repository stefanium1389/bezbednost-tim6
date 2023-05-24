import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckCertValidityComponent } from './check-cert-validity.component';

describe('CheckCertValidityComponent', () => {
  let component: CheckCertValidityComponent;
  let fixture: ComponentFixture<CheckCertValidityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckCertValidityComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckCertValidityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
