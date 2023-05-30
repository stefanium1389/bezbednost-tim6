import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../backend-services/login.service';
import { LoginRequest } from '../dtos/LoginDtos';
import { JwtService } from '../jwt.service';
import { UserdataService } from '../backend-services/userdata.service';
import { ReCaptchaV3Service } from 'ng-recaptcha';
import { ApiResponse } from '../dtos/RecaptchaApiResponse';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(private jwtService: JwtService, private userService:LoginService, private router:Router, private users:UserdataService, private recaptchaV3Service: ReCaptchaV3Service) { }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('',Validators.required),
      password: new FormControl('', Validators.required)
    });
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
}
