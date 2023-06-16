import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TfaServiceService {

  getToken() {
    return sessionStorage.getItem('tfaToken');
  }

  setToken(accessToken: string) {
    sessionStorage.setItem('tfaToken',accessToken);
  }

  constructor() { }
}
