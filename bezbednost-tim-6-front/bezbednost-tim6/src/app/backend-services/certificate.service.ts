import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http: HttpClient) { }

  checkIsValid(serialNumber: string):Observable<any>{
    return this.http.get(environment.apiUrl+`/cert/isValid/${serialNumber}`);
  }
}
