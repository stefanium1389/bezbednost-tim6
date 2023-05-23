import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from '../dtos/LoginDtos';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http : HttpClient) { }

  login(body: LoginRequest):Observable<LoginResponse>{
    return this.http.post<LoginResponse>(`${environment.apiUrl}/user/login`, body) 
  }
  refreshToken(body: LoginResponse):Observable<any>{
    return this.http.post<any>(`${environment.apiUrl}/user/refreshToken`, body)
  }
}
