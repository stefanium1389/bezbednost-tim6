import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterResponseDTO, RegistrationDTO } from '../dtos/RegistrationDtos';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { SuccessDTO } from '../dtos/MessageDtos';
import { CodeAndPasswordDTO, PasswordResetRequestDTO } from '../dtos/ResetPasswordDtos';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {

  constructor(private http: HttpClient) { }

  register(body: RegistrationDTO){

  }

  sendPasswordResetEmail(dto: PasswordResetRequestDTO):Observable<SuccessDTO> {
    return this.http.post<SuccessDTO>(environment.apiUrl+`/user/resetPassword`,dto);
  }
  checkResetPassword(dto: CodeAndPasswordDTO):Observable<SuccessDTO>{
    return this.http.put<SuccessDTO>(environment.apiUrl+`/user/resetPassword`,dto);
  }

  registerUser(dto: RegistrationDTO):Observable<RegisterResponseDTO>{
    return this.http.post<RegisterResponseDTO>(environment.apiUrl+`/user/register`,dto);
  }
}
