import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrationComponent } from './registration/registration.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginComponent } from './login/login.component';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { HomeComponent } from './home/home.component';
import { AddJournalComponent } from './add-journal/add-journal.component';
import { ActivateJournalComponent } from './activate-journal/activate-journal.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AdminJournalsComponent } from './admin-journals/admin-journals.component';
import { AdminRegistrationsComponent } from './admin-registrations/admin-registrations.component';
import { UrednikJournalsComponent } from './urednik-journals/urednik-journals.component';
import { UrednikRegisterComponent } from './urednik-register/urednik-register.component';




@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    NavbarComponent,
    LoginComponent,
    HomeComponent,
    AddJournalComponent,
    ActivateJournalComponent,
    AdminJournalsComponent,
    AdminRegistrationsComponent,
    UrednikJournalsComponent,
    UrednikRegisterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgMultiSelectDropDownModule.forRoot()
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
