import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { UserMainComponent } from './user-main/user-main.component';
import { SmsValidationComponent } from './sms-validation/sms-validation.component';

const routes: Routes = [
  {path: "", component: LoginComponent},
  {path: "register", component: RegisterComponent},
  {path: "reset-password", component: ResetPasswordComponent},
  {path: "activate", component: VerifyEmailComponent},
  {path: "user-main", component: UserMainComponent},
  {path: "sms-validation", component: SmsValidationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
