import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminJournalsComponent } from './admin-journals.component';

describe('AdminJournalsComponent', () => {
  let component: AdminJournalsComponent;
  let fixture: ComponentFixture<AdminJournalsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminJournalsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminJournalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
