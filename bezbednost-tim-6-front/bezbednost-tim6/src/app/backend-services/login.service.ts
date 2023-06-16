import { HttpClient, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse, LoginCreateCode,LoginSecondStepRequest } from '../dtos/LoginDtos';
import { environment } from 'src/environments/environment';
import { SuccessDTO } from '../dtos/MessageDtos';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http : HttpClient) { }

  loginStepOne(body: LoginRequest):Observable<LoginCreateCode>{
    return this.http.post<LoginCreateCode>(`${environment.apiUrl}/user/login/first`, body);
  }
  loginStepTwo(body: LoginSecondStepRequest):Observable<LoginResponse>{
    return this.http.post<LoginResponse>(`${environment.apiUrl}/user/login/second`, body);
  }
  refreshToken(body: LoginResponse):Observable<SuccessDTO>{
    return this.http.post<SuccessDTO>(`${environment.apiUrl}/user/refreshToken`, body);
  }
  loginWithGoogle(credential: string):Observable<LoginResponse> {
    const header = new HttpHeaders().set('Content-type', 'application/json');
    return this.http.post<LoginResponse>(`${environment.apiUrl}/user/loginWithGoogle`,JSON.stringify(credential), {headers: header});
  }
}
