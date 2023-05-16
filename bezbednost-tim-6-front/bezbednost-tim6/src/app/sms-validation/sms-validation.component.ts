import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserdataService } from '../backend-services/userdata.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sms-validation',
  templateUrl: './sms-validation.component.html',
  styleUrls: ['./sms-validation.component.scss']
})
export class SmsValidationComponent implements OnInit {

  constructor(private userData: UserdataService, private router: Router) { }
  smsCodeForm!: FormGroup;

  ngOnInit(): void {
    this.smsCodeForm = new FormGroup({
      smsCode: new FormControl('',Validators.required)
    });
  }

  checkCode(){
    const code = this.smsCodeForm.get("smsCode")?.value;
    this.userData.activateUser(code).subscribe({
      next: result => {
        alert(result.message)
        this.router.navigate(['']);
      },
      error: error => {
        alert(error.error.message)
      }
    });
  }
}
