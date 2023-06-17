import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'; // Import HttpClientModule

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserMainComponent } from './user-main/user-main.component';
import { AdminMainComponent } from './admin-main/admin-main.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { SmsValidationComponent } from './sms-validation/sms-validation.component';
import { CheckCertValidityComponent } from './check-cert-validity/check-cert-validity.component';
import { JwtInterceptorService } from './jwt-interceptor.service';
import { RequestCertComponent } from './request-cert/request-cert.component';
import { ViewAllCertsComponent } from './view-all-certs/view-all-certs.component';
import { ErrorInterceptorService } from './error-interceptor.service';
import { ViewSentRequestsComponent } from './view-sent-requests/view-sent-requests.component';
import { ViewReceivedRequestsComponent } from './view-received-requests/view-received-requests.component';
import { InputReasonComponent } from './input-reason/input-reason.component';
import {MatDialogModule} from "@angular/material/dialog";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ViewAllRequestsComponent } from './view-all-requests/view-all-requests.component';
import { RenewPasswordComponent } from './renew-password/renew-password.component';
import { MatSelectModule } from '@angular/material/select';
import { RecaptchaV3Module, RECAPTCHA_V3_SITE_KEY } from 'ng-recaptcha';
import { environment } from 'src/environments/environment';
import { TwoFactorAuthComponent } from './two-factor-auth/two-factor-auth.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserMainComponent,
    AdminMainComponent,
    ResetPasswordComponent,
    VerifyEmailComponent,
    SmsValidationComponent,
    CheckCertValidityComponent,
    RequestCertComponent,
    ViewAllCertsComponent,
    ViewSentRequestsComponent,
    ViewReceivedRequestsComponent,
    InputReasonComponent,
    ViewAllRequestsComponent,
    RenewPasswordComponent,
    TwoFactorAuthComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    MatDialogModule,
    FormsModule,
    BrowserAnimationsModule,
    MatSelectModule,
    RecaptchaV3Module
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptorService,
      multi: true
    },
    {
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptorService,
    multi: true
    },
    {
      provide: RECAPTCHA_V3_SITE_KEY,
      useValue: environment.recaptcha.siteKey,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
