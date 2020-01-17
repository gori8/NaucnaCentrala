import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UrednikRegisterComponent } from './urednik-register.component';

describe('UrednikRegisterComponent', () => {
  let component: UrednikRegisterComponent;
  let fixture: ComponentFixture<UrednikRegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UrednikRegisterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UrednikRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
