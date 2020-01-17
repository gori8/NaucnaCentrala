import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRegistrationsComponent } from './admin-registrations.component';

describe('AdminRegistrationsComponent', () => {
  let component: AdminRegistrationsComponent;
  let fixture: ComponentFixture<AdminRegistrationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminRegistrationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminRegistrationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
