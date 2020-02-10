import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IzborPlacanjaComponent } from './izbor-placanja.component';

describe('IzborPlacanjaComponent', () => {
  let component: IzborPlacanjaComponent;
  let fixture: ComponentFixture<IzborPlacanjaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IzborPlacanjaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IzborPlacanjaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
