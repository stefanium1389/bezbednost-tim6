import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSentRequestsComponent } from './view-sent-requests.component';

describe('ViewSentRequestsComponent', () => {
  let component: ViewSentRequestsComponent;
  let fixture: ComponentFixture<ViewSentRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewSentRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewSentRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
