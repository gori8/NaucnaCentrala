import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormaPlacanjaComponent } from './components/forma-placanja/forma-placanja.component';
import { IzborPlacanjaComponent } from './components/izbor-placanja/izbor-placanja.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from './navbar/navbar.component';
import { PaypalSubscriptionComponent } from './components/paypal-subscription/paypal-subscription.component'


@NgModule({
  declarations: [
    AppComponent,
    FormaPlacanjaComponent,
    IzborPlacanjaComponent,
    NavbarComponent,
    PaypalSubscriptionComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
   
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
