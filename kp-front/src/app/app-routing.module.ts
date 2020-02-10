import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormaPlacanjaComponent } from './components/forma-placanja/forma-placanja.component';
import { IzborPlacanjaComponent } from './components/izbor-placanja/izbor-placanja.component';
import { PaypalSubscriptionComponent } from './components/paypal-subscription/paypal-subscription.component';

const routes: Routes = [
  { path: 'paymentform/:nacinPlacanja/:uuid', component: FormaPlacanjaComponent },
  { path: ':uuid', component: IzborPlacanjaComponent },
  { path: 'paypal/subscription/:uuid', component: PaypalSubscriptionComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
