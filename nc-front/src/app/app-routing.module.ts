import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AddJournalComponent } from './add-journal/add-journal.component';
import { AuthGuard, AdminGuard } from './_guards';
import { ActivateJournalComponent } from './activate-journal/activate-journal.component';
import { AdminJournalsComponent } from './admin-journals/admin-journals.component';
import { AdminRegistrationsComponent } from './admin-registrations/admin-registrations.component';
import { UrednikJournalsComponent } from './urednik-journals/urednik-journals.component';
import { UrednikRegisterComponent } from './urednik-register/urednik-register.component';
import { SuccessPageComponent } from './success-page/success-page.component';
import { AuthorSelectJournalComponent } from './author-select-journal/author-select-journal.component';
import { AuthorGuard } from './_guards/author.guard';
import { AuthorInputComponent } from './author-input/author-input.component';
import { ShowPdfComponent } from './show-pdf/show-pdf.component';
import { UserTasksComponent } from './user-tasks/user-tasks.component';
import { ActiveTaskComponent } from './active-task/active-task.component';


const routes: Routes = [
	{ path: '', component: HomeComponent },
  { path: 'reg/success', component: SuccessPageComponent },
	{ path: 'journal/activate/:processInstanceID', component: ActivateJournalComponent, canActivate: [AuthGuard] },
	{ path: 'journal/add', component: AddJournalComponent, canActivate: [AuthGuard] },
  { path: 'urednik/journals', component: UrednikJournalsComponent, canActivate: [AuthGuard] },
	{ path: 'admin/journals', component: AdminJournalsComponent, canActivate: [AdminGuard] },
  { path: 'admin/users', component: AdminRegistrationsComponent, canActivate: [AdminGuard] },
  { path: 'admin/register/urednik', component: UrednikRegisterComponent, canActivate: [AdminGuard] },
  { path: 'author/select/journal', component: AuthorSelectJournalComponent, canActivate: [AuthorGuard] },
  { path: 'input/paper/:processInstanceID', component: AuthorInputComponent, canActivate: [AuthorGuard] },
  { path: 'pdf', component: ShowPdfComponent, canActivate: [AuthorGuard] },
	{ path: 'register', component: RegistrationComponent },
  { path: 'tasks', component: UserTasksComponent },
  { path: 'task/:taskID', component: ActiveTaskComponent },
	{ path: 'login', component: LoginComponent },
	{ path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
