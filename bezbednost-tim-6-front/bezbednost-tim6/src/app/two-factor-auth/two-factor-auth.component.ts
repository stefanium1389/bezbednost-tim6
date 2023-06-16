import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../backend-services/login.service';
import { LoginRequest, LoginSecondStepRequest } from '../dtos/LoginDtos';
import { JwtService } from '../jwt.service';
import { TfaServiceService } from '../tfa-service.service';

@Component({
  selector: 'app-two-factor-auth',
  templateUrl: './two-factor-auth.component.html',
  styleUrls: ['./two-factor-auth.component.scss']
})
export class TwoFactorAuthComponent implements OnInit {

  tfaForm!: FormGroup;

  constructor(private tfaService: TfaServiceService, private jwtService: JwtService, private router: Router, private loginService: LoginService) { }

  ngOnInit(): void {
    this.tfaForm = new FormGroup({
      code: new FormControl('', [Validators.required])
    });
  }
  

  submitCode() {
    const code = this.tfaForm.get('code')?.value;
    const body: LoginSecondStepRequest = {
      token: this.tfaService.getToken(),
      code: code
    };

    this.loginService.loginStepTwo(body).subscribe({
      next: result => {
        this.jwtService.setAccessToken(result.accessToken);
        this.jwtService.setRefreshToken(result.refreshToken);
        if (this.jwtService.getRole() === 'ROLE_USER' || this.jwtService.getRole() === 'ROLE_ADMIN') {
          this.router.navigate(['main']);
        } else {
          console.log(this.jwtService.getRole());
        }
      },
      error: error => {
        if (error?.error?.message != undefined) {
          alert(error?.error?.message);
        }
        if (error?.status == 307) {
          console.log('idemoo');
        }
      }
    });
  }
}
