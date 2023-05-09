import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  constructor(private http: HttpClient, private router: Router) { }

  registerForm!: FormGroup;
  selectedOption!: string;

  ngOnInit(): void {
    this.registerForm = new FormGroup({

      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      repeatPassword: new FormControl('', Validators.required),
      name: new FormControl('', Validators.required),
      surname: new FormControl('', Validators.required),
      phone: new FormControl('', Validators.required),
      address: new FormControl('', Validators.required)}
      , { validators: matchPasswords }
    );
  }

  register(){
    if(this.registerForm.invalid){
      alert(`popunite formu!`);
      return;
    }
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
