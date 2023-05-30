import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistrationDTO } from '../dtos/RegistrationDtos';
import { UserdataService } from '../backend-services/userdata.service';
import { ReCaptchaV3Service } from 'ng-recaptcha';
import { ApiResponse } from '../dtos/RecaptchaApiResponse'; 


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private userData: UserdataService,private router: Router, private recaptchaV3Service: ReCaptchaV3Service) { }

  registerForm!: FormGroup;

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      repeatPassword: new FormControl('', Validators.required),
      name: new FormControl('', Validators.required),
      surname: new FormControl('', Validators.required),
      phone: new FormControl('', Validators.required),
      validationType: new FormControl('', Validators.required)},
      { validators: matchPasswords }
    );
  }

  register(){
    this.recaptchaV3Service.execute('importantAction')
      .subscribe((token) => {
        let tokenDTO: ApiResponse = {
            token: token,
        }
        this.userData.recaptcha(tokenDTO).subscribe({
          next: result => {
            console.log("jej");
            if(this.registerForm.invalid){
              alert('popunite formu');
              console.log('email:',this.registerForm.get('email')?.valid)
              console.log('password:',this.registerForm.get('password')?.valid)
              console.log('repeatPassword:',this.registerForm.get('repeatPassword')?.valid)
              console.log('name:',this.registerForm.get('name')?.valid)
              console.log('phone:',this.registerForm.get('phone')?.valid)
              console.log('validationType',this.registerForm.get('validationType')?.valid)
        
              return;
            }
            const dto: RegistrationDTO = {
              email: this.registerForm.get('email')?.value,
              password: this.registerForm.get('password')?.value,
              name: this.registerForm.get('name')?.value,
              telephoneNumber: this.registerForm.get('phone')?.value,
              surname: this.registerForm.get('surname')?.value,
              validationType: this.registerForm.get('validationType')?.value,
            }
            this.userData.registerUser(dto).subscribe({
              next: result => {
                alert('wellcome, '+ result.name)
                if(this.registerForm.get('validationType')?.value == "emailValidation"){
                  this.router.navigate([""]);
                }
                else{
                  this.router.navigate(["sms-validation"]);
                }
              },
              error: e =>
          {console.log(e.message);
          alert(e?.error?.message);
          this.registerForm.reset();
          }
            })
          },
          error: e =>
          {console.log(e.message);
          alert(e.message);
          this.registerForm.reset();
          }
        })
      });
  }
}


export function matchPasswords(control: AbstractControl) {
  const password = control.get('password');
  const confirmPassword = control.get('repeatPassword');
  if (password?.value !== confirmPassword?.value) {
    return { notSame: true };
  }
  return null;
}
