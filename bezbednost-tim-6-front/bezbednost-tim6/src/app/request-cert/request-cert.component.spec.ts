import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestCertComponent } from './request-cert.component';

describe('RequestCertComponent', () => {
  let component: RequestCertComponent;
  let fixture: ComponentFixture<RequestCertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestCertComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestCertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
