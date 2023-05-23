import { Injectable } from '@angular/core';
import jwtDecode, { JwtPayload } from 'jwt-decode';

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

  logout(){
    sessionStorage.clear();
  }

  getEmail() : string | undefined{
    const jwt = this.getAccessToken();
    if(jwt){
      const decoded = jwtDecode(jwt) as JwtPayload;
      return decoded.sub; 
    }
    return undefined;
  }

  getRole() : string | undefined{
    const jwt = this.getAccessToken();
    if(jwt){
      const decoded = jwtDecode(jwt) as {role: string};
      return decoded.role; 
    }
    return undefined;
  }

  constructor() { }
}

