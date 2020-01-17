import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateJournalComponent } from './activate-journal.component';

describe('ActivateJournalComponent', () => {
  let component: ActivateJournalComponent;
  let fixture: ComponentFixture<ActivateJournalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivateJournalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivateJournalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
