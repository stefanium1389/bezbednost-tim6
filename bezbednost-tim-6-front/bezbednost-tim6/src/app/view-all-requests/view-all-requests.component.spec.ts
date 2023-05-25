import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllRequestsComponent } from './view-all-requests.component';

describe('ViewAllRequestsComponent', () => {
  let component: ViewAllRequestsComponent;
  let fixture: ComponentFixture<ViewAllRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewAllRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
