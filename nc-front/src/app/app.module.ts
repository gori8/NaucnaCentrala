import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrationComponent } from './registration/registration.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NavbarComponent } from './_navbar/navbar.component';
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
import { SuccessPageComponent } from './success-page/success-page.component';
import { NotifierModule } from "angular-notifier";
import { AuthorSelectJournalComponent } from './author-select-journal/author-select-journal.component';
import { AuthorInputComponent } from './author-input/author-input.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FileUploadModule } from 'ng2-file-upload';
import { ShowPdfComponent } from './show-pdf/show-pdf.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { ActiveTaskComponent } from './active-task/active-task.component';
import { UserTasksComponent } from './user-tasks/user-tasks.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { SearchResultComponentComponent } from './search-result-component/search-result-component.component';







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
    UrednikRegisterComponent,
    SuccessPageComponent,
    AuthorSelectJournalComponent,
    AuthorInputComponent,
    ShowPdfComponent,
    ActiveTaskComponent,
    UserTasksComponent,
    AdvancedSearchComponent,
    SearchResultComponentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgMultiSelectDropDownModule.forRoot(),
    NotifierModule,
    NgbModule,
    FileUploadModule,
    NgxExtendedPdfViewerModule,
    BrowserAnimationsModule,
    MatIconModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
