import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UrednikJournalsComponent } from './urednik-journals.component';

describe('UrednikJournalsComponent', () => {
  let component: UrednikJournalsComponent;
  let fixture: ComponentFixture<UrednikJournalsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UrednikJournalsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UrednikJournalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
