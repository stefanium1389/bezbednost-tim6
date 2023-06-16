import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../backend-services/login.service';
import { LoginRequest } from '../dtos/LoginDtos';
import { JwtService } from '../jwt.service';
import { UserdataService } from '../backend-services/userdata.service';
import { ReCaptchaV3Service } from 'ng-recaptcha';
import { ApiResponse } from '../dtos/RecaptchaApiResponse';
import { CredentialResponse, PromptMomentNotification } from 'google-one-tap';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  isDisabled: boolean = true;

  constructor(private jwtService: JwtService, private userService:LoginService, private router:Router, private users:UserdataService, private recaptchaV3Service: ReCaptchaV3Service) { 
    this.check = this.check.bind(this);
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('',Validators.required),
      password: new FormControl('', Validators.required)},
      { validators: this.check },
    );

    //@ts-ignore
    window.onGoogleLibraryLoad = () => {
      //@ts-ignore
      google.accounts.id.initialize({
        client_id:"157255898883-5r581em5hin572chhilc6h4k40b0gd3t.apps.googleusercontent.com",
        callback: this.handleCredentialResponse.bind(this),
        auto_select: false,
        cancel_on_tap_outside: true
      });
      //@ts-ignore
      google.accounts.id.renderButton(
        //@ts-ignore
        document.getElementById("googleButtonDiv"),
        {theme: 'outline', size: 'large', width:'100%'}
      );
      //@ts-ignore
      google.accounts.id.prompt((notification:PromptMomentNotification) => {})

    }
  }

  /*
  login(){

    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;
    const body: LoginRequest = {
      email: email,
      password: password
    }
    this.userService.login(body).subscribe({
      next: result => {
        this.jwtService.setAccessToken(result.accessToken);
        this.jwtService.setRefreshToken(result.refreshToken);
        if(this.jwtService.getRole() === 'ROLE_USER')
        {this.router.navigate(['user-main']);}

        // PAZI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        else if(this.jwtService.getRole() === 'ROLE_ADMIN')
        {this.router.navigate(['admin-main']);}
        //{this.router.navigate(['user-main']);}
        else {console.log(this.jwtService.getRole())}
      },
      error: error => {
        alert(error.error.message);
      }
    })
  }
  */

  login(){

    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;
    const body: LoginRequest = {
      email: email,
      password: password
    }
    this.recaptchaV3Service.execute('importantAction')
      .subscribe((token) => {
        let tokenDTO: ApiResponse = {
            token: token,
        }
        this.users.recaptcha(tokenDTO).subscribe({
          next: result => {
            console.log("jej");
            this.userService.login(body).subscribe({
              next: result => {
                this.jwtService.setAccessToken(result.accessToken);
                this.jwtService.setRefreshToken(result.refreshToken);
                if(this.jwtService.getRole() === 'ROLE_USER' || this.jwtService.getRole() === 'ROLE_ADMIN') {
                  this.router.navigate(['main']);
                } else {console.log(this.jwtService.getRole())}
              },
              error: error => {
                if (error?.error?.message != undefined) {
                  alert(error?.error?.message);
                }
                if (error?.status == 307) {
                    console.log('idemoo');
                    //this.router.navigate(["renew-password"]);
                    //this.sendRenew();
                }
                
              }
            })
          },
          error: e =>
          {console.log(e.message);
          alert(e.message);
          this.loginForm.reset();
          }
        })
      });

  }



  sendRenew(){
    this.users.sendPasswordRenewEmail({email:this.loginForm.get('email')?.value}).subscribe({
        next: () => {
        }
      })
  };

  handleCredentialResponse(response: CredentialResponse){
    this.userService.loginWithGoogle(response.credential).subscribe({
      next: result => {
        this.jwtService.setAccessToken(result.accessToken);
        this.jwtService.setRefreshToken(result.refreshToken);
        if(this.jwtService.getRole() === 'ROLE_USER' || this.jwtService.getRole() === 'ROLE_ADMIN') {
          this.router.navigate(['main']).then(()=>{location.reload();});
        } else {console.log(this.jwtService.getRole())}
      },
      error: error => {
        if (error?.error?.message != undefined) {
          alert(error?.error?.message);
        }
        if (error?.status == 307) {
            console.log('idemoo');
            this.router.navigate(["renew-password"]);
            this.sendRenew();
        }
        
      }
    })
  }

  check(control: AbstractControl) {
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{};:,<.>])(?=.*[^\s]).{8,}$/;
    const password = control.get('password');
    const isValidPassword = passwordRegex.test(password?.value);
    const cmail = control.get('email');
    const isValidEmail = emailRegex.test(cmail?.value);
    if (isValidEmail && isValidPassword) {
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
    const errors: { [key: string]: any } = {};
    if (!isValidEmail) {
      errors['validEmail'] = true;
    }
    if (!isValidPassword) {
      errors['validPassword'] = true;
    }
    return Object.keys(errors).length > 0 ? errors : null;
    
  }

}
