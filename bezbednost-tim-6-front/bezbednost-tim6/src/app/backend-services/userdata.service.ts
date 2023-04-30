import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationDTO } from '../dtos/RegistrationDtos';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {

  constructor(private http: HttpClient) { }

  register(body: RegistrationDTO){

  }

  checkResetPassword(newPassword: string, code: string) {
    
  }
  sendPasswordResetEmail(email: string) {
    
  }


}
