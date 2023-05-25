import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputReasonComponent } from './input-reason.component';

describe('InputReasonComponent', () => {
  let component: InputReasonComponent;
  let fixture: ComponentFixture<InputReasonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputReasonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputReasonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
