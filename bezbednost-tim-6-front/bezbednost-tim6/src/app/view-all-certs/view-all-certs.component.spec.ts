import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllCertsComponent } from './view-all-certs.component';

describe('ViewAllCertsComponent', () => {
  let component: ViewAllCertsComponent;
  let fixture: ComponentFixture<ViewAllCertsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllCertsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewAllCertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
