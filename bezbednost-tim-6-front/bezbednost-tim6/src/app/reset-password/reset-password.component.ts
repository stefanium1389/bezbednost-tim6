import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserdataService } from '../backend-services/userdata.service';
import { matchPasswords } from '../register/register.component';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  emailForm!: FormGroup;
  passwordForm!: FormGroup;
  codeSent: boolean = false;
  hasToken: boolean = false;
  byPhone: boolean = false;
  SMSPasswordForm!: FormGroup;
  ;
  token!:string;
  selectModeForm: FormControl;

  constructor(private router: Router, private users:UserdataService, private route:ActivatedRoute) {
    this.selectModeForm = new FormControl();
   }

  ngOnInit(): void {
    this.selectModeForm = new FormControl('email');
    this.emailForm = new FormGroup({
      email: new FormControl('',Validators.required)
    });
    this.passwordForm = new FormGroup({
      password: new FormControl('',Validators.required),
      repeatPassword: new FormControl('',Validators.required)
    }, { validators: matchPasswords });
    this.SMSPasswordForm = new FormGroup({
      code: new FormControl('', Validators.required),
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
      this.users.sendPasswordReset({email:this.emailForm.get('email')?.value, mode:'email'}).subscribe({
        next: () => {
          this.codeSent = true;
        }
      })
      this.codeSent = true;
    }
  };
  sendResetPhone(){
    if(this.emailForm.get('email')?.value){
      this.users.sendPasswordReset({email:this.emailForm.get('email')?.value, mode:'phone'}).subscribe({
        next: () => {
          this.codeSent = true;
          this.byPhone = true;
        }
      })
      this.codeSent = true;
      this.byPhone = true;
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
  confirmResetSMS(){
    if(this.SMSPasswordForm.get('password')?.value){
      this.users.checkResetPassword({newPassword:this.SMSPasswordForm.get('password')?.value,repeatPassword:this.SMSPasswordForm.get('repeatPassword')?.value, code:this.SMSPasswordForm.get('code')?.value}).subscribe({
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





  
  
  


