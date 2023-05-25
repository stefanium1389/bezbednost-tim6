import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { UserMainComponent } from './user-main/user-main.component';
import { SmsValidationComponent } from './sms-validation/sms-validation.component';
import { CheckCertValidityComponent } from './check-cert-validity/check-cert-validity.component';
import { RequestCertComponent } from './request-cert/request-cert.component';
import { ViewAllCertsComponent } from './view-all-certs/view-all-certs.component';
import { ViewSentRequestsComponent } from './view-sent-requests/view-sent-requests.component';
import { ViewReceivedRequestsComponent } from './view-received-requests/view-received-requests.component';

const routes: Routes = [
  {path: "", component: LoginComponent},
  {path: "register", component: RegisterComponent},
  {path: "reset-password", component: ResetPasswordComponent},
  {path: "activate", component: VerifyEmailComponent},
  {path: "user-main", component: UserMainComponent},
  {path: "sms-validation", component: SmsValidationComponent},
  {path: "check-cert-validity", component: CheckCertValidityComponent},
  {path: "request-cert", component: RequestCertComponent},
  {path: "view-all-certs", component: ViewAllCertsComponent},
  {path: "view-sent-requests", component: ViewSentRequestsComponent},
  {path: "view-received-requests", component: ViewReceivedRequestsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
