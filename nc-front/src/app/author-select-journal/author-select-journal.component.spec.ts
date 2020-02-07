import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorSelectJournalComponent } from './author-select-journal.component';

describe('AuthorSelectJournalComponent', () => {
  let component: AuthorSelectJournalComponent;
  let fixture: ComponentFixture<AuthorSelectJournalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorSelectJournalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorSelectJournalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
