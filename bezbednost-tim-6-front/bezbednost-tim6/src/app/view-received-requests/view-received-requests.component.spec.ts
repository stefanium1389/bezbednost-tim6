import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewReceivedRequestsComponent } from './view-received-requests.component';

describe('ViewReceivedRequestsComponent', () => {
  let component: ViewReceivedRequestsComponent;
  let fixture: ComponentFixture<ViewReceivedRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewReceivedRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewReceivedRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
