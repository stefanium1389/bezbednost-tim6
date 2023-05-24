import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserdataService } from '../backend-services/userdata.service';
import { matchPasswords } from '../register/register.component';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  emailForm!: FormGroup;
  passwordForm!: FormGroup;
  emailSent: boolean = false;
  hasToken: boolean = false;
  token!:string;

  constructor(private router: Router, private users:UserdataService, private route:ActivatedRoute) { }

  ngOnInit(): void {
    this.emailForm = new FormGroup({
      email: new FormControl('',Validators.required)
    });
    this.passwordForm = new FormGroup({
      password: new FormControl('',Validators.required),
      repeatPassword: new FormControl('',Validators.required)
    }, { validators: matchPasswords });
    this.route.queryParams.subscribe(params => {
      let token = params['token'];
      this.token = token;
      if(this.token) {this.hasToken = true;}
    });
  }

  sendReset(){
    if(this.emailForm.get('email')?.value){
      this.users.sendPasswordResetEmail({email:this.emailForm.get('email')?.value}).subscribe({
        next: () => {
          this.emailSent = true;
        }
      })
      this.emailSent = true;
    }
  };

  confirmReset(){
    if(this.passwordForm.get('password')?.value){
      this.users.checkResetPassword({newPassword:this.passwordForm.get('password')?.value,repeatPassword:this.passwordForm.get('repeatPassword')?.value, code:this.token}).subscribe({
        next: (result: any) => {
          console.log(result);
          this.backToLogin();
        },
        error: (error) => {
          alert(error.message);
        }
      })
    }
  }

  backToLogin(){
    this.router.navigate(['..']);
  };
}





  
  
  


