import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormaPlacanjaComponent } from './forma-placanja.component';

describe('FormaPlacanjaComponent', () => {
  let component: FormaPlacanjaComponent;
  let fixture: ComponentFixture<FormaPlacanjaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormaPlacanjaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormaPlacanjaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
