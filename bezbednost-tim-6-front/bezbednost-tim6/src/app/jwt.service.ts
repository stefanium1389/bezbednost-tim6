import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtService {
  
  getAccessToken() {
    return sessionStorage.getItem('accessToken');
  }

  setAccessToken(accessToken: string) {
    sessionStorage.setItem('accessToken',accessToken);
  }

  getRefreshToken() {
    return sessionStorage.getItem('refreshToken');
  }

  setRefreshToken(refreshToken: string) {
    sessionStorage.setItem('refreshToken',refreshToken);
  }

  constructor() { }
}
