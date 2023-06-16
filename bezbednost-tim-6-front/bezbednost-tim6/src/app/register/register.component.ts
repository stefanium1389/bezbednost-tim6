import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators, ValidatorFn } from '@angular/forms';
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

  constructor(private userData: UserdataService,private router: Router, private recaptchaV3Service: ReCaptchaV3Service) {
    this.check = this.check.bind(this);
   }

  registerForm!: FormGroup;
  isDisabled: boolean = true;

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      repeatPassword: new FormControl('', Validators.required),
      name: new FormControl('', Validators.required),
      surname: new FormControl('', Validators.required),
      phone: new FormControl('', Validators.required),
      validationType: new FormControl('', Validators.required),
      btn: new FormControl("")},
      { validators: this.check },
    );
  }

  	customEmailValidator(control: AbstractControl) {
      const custom = control.get('email');
      if (custom?.value == "hej") {
        return { email: true };
      }
      return null;
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

   check(control: AbstractControl) {
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{};:,<.>])(?=.*[^\s]).{8,}$/;
    const lettersOnlyRegex = /^[A-Za-z]+$/;
    const numbersOnlyRegex = /^\d+$/;
    const password = control.get('password');
    const isValidPassword = passwordRegex.test(password?.value);
    const confirmPassword = control.get('repeatPassword');
    const isValidRepeatPassword = passwordRegex.test(confirmPassword?.value);
    const passwordMatch = password?.value !== confirmPassword?.value;
    const cmail = control.get('email');
    const isValidEmail = emailRegex.test(cmail?.value);
    const name = control.get('name');
    const isValidName = lettersOnlyRegex.test(name?.value);
    const surname = control.get('surname');
    const isValidSurname = lettersOnlyRegex.test(surname?.value);
    const phoneNumber = control.get('phone');
    const isPhoneValid = numbersOnlyRegex.test(phoneNumber?.value);
    if (isValidEmail && isValidPassword && isValidRepeatPassword && !passwordMatch && isValidName && isValidSurname && isPhoneValid) {
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
    return { notSame: passwordMatch,
          validEmail: !isValidEmail,
          validPassword :!isValidPassword,
          validRepeatPassword: !isValidRepeatPassword,
          validName: !isValidName,
          validSurname: !isValidSurname,
          validPN: !isPhoneValid
          };
    
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

// export function check(control: AbstractControl) {
//   const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
//   const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{};:,<.>])(?=.*[^\s]).{8,}$/;
//   const lettersOnlyRegex = /^[A-Za-z]+$/;
//   const numbersOnlyRegex = /^\d+$/;
//   const password = control.get('password');
//   const isValidPassword = passwordRegex.test(password?.value);
//   const confirmPassword = control.get('repeatPassword');
//   const isValidRepeatPassword = passwordRegex.test(confirmPassword?.value);
//   const passwordMatch = password?.value !== confirmPassword?.value;
//   const cmail = control.get('email');
//   const isValidEmail = emailRegex.test(cmail?.value);
//   const name = control.get('name');
//   const isValidName = lettersOnlyRegex.test(name?.value);
//   const surname = control.get('surname');
//   const isValidSurname = lettersOnlyRegex.test(surname?.value);
//   const phoneNumber = control.get('phone');
//   const isPhoneValid = numbersOnlyRegex.test(phoneNumber?.value);
//   const btnControl = control.get('btn');
//   // if (btnControl && isValidEmail && isValidPassword && isValidRepeatPassword && passwordMatch && isValidName && isValidSurname && isPhoneValid) {
//   //   const btnElement: HTMLButtonElement = btnControl.value;
//   //   btnElement.disabled = false;
//   // } else if (btnControl && !(isValidEmail && isValidPassword && isValidRepeatPassword && passwordMatch && isValidName && isValidSurname && isPhoneValid) ) {
//   //   const btnElement: HTMLButtonElement = btnControl.value;
//   //   btnElement.disabled = true;
//   // }
//   return { notSame: passwordMatch,
//         validEmail: !isValidEmail,
//         validPassword :!isValidPassword,
//         validRepeatPassword: !isValidRepeatPassword,
//         validName: !isValidName,
//         validSurname: !isValidSurname,
//         validPN: !isPhoneValid
//         };
  
// }
