import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserdataService } from '../backend-services/userdata.service';
import { matchPasswords } from '../register/register.component';

@Component({
  selector: 'app-renew-password',
  templateUrl: './renew-password.component.html',
  styleUrls: ['./renew-password.component.css']
})
export class RenewPasswordComponent implements OnInit {

  passwordForm!: FormGroup;
  hasToken: boolean = false;
  token!:string;

  constructor(private router: Router, private users:UserdataService, private route:ActivatedRoute) { }

  ngOnInit(): void {
    this.passwordForm = new FormGroup({
      oldPassword: new FormControl(''),
      password: new FormControl('',Validators.required),
      repeatPassword: new FormControl('',Validators.required)
    }, { validators: matchPasswords });
    this.route.queryParams.subscribe(params => {
      let token = params['token'];
      this.token = token;
      if(this.token) {this.hasToken = true;}
    });
  }

  confirmRenew(){
    if(this.passwordForm.get('password')?.value && this.passwordForm.get('repeatPassword')?.value && this.passwordForm.get('oldPassword')?.value){
      this.users.checkRenewPassword({oldPassword:this.passwordForm.get('oldPassword')?.value,newPassword:this.passwordForm.get('password')?.value,repeatPassword:this.passwordForm.get('repeatPassword')?.value, code:this.token}).subscribe({
        next: (result: any) => {
          console.log(result);
          this.backToLogin();
        },
        error: (error) => {
          alert(error.error.message);
        }
      })
    }
  }

  backToLogin(){
    this.router.navigate(['..']);
  };

}
