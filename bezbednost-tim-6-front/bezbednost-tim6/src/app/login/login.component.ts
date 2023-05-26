import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../backend-services/login.service';
import { LoginRequest } from '../dtos/LoginDtos';
import { JwtService } from '../jwt.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(private jwtService: JwtService, private userService:LoginService, private router:Router) { }

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
    this.userService.login(body).subscribe({
      next: result => {
        this.jwtService.setAccessToken(result.accessToken);
        this.jwtService.setRefreshToken(result.refreshToken);
        if(this.jwtService.getRole() === 'ROLE_USER' || this.jwtService.getRole() === 'ROLE_ADMIN') {
          this.router.navigate(['main']);
        } else {console.log(this.jwtService.getRole())}
      },
      error: error => {
        alert(error.error.message);
      }
    })

  }
}
