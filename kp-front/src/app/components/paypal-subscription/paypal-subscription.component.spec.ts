import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaypalSubscriptionComponent } from './paypal-subscription.component';

describe('PaypalSubscriptionComponent', () => {
  let component: PaypalSubscriptionComponent;
  let fixture: ComponentFixture<PaypalSubscriptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaypalSubscriptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaypalSubscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
