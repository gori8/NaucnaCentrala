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


const routes: Routes = [
	{ path: '', component: HomeComponent },
  { path: 'reg/success', component: SuccessPageComponent },
	{ path: 'journal/activate/:processInstanceID', component: ActivateJournalComponent, canActivate: [AuthGuard] },
	{ path: 'journal/add', component: AddJournalComponent, canActivate: [AuthGuard] },
  { path: 'urednik/journals', component: UrednikJournalsComponent, canActivate: [AuthGuard] },
	{ path: 'admin/journals', component: AdminJournalsComponent, canActivate: [AdminGuard] },
  { path: 'admin/users', component: AdminRegistrationsComponent, canActivate: [AdminGuard] },
  { path: 'admin/register/urednik', component: UrednikRegisterComponent, canActivate: [AdminGuard] },
	{ path: 'register', component: RegistrationComponent },
	{ path: 'login', component: LoginComponent },
	{ path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
