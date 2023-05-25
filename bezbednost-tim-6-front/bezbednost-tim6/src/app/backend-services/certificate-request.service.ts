import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Reason } from '../view-received-requests/view-received-requests.component';

@Injectable({
  providedIn: 'root'
})
export class CertificateRequestService {

  constructor(private http: HttpClient) { }

  getReceived():Observable<any>{
    return this.http.get(environment.apiUrl+`/cert/request/received/view`);
  }

  getSent():Observable<any>{
    return this.http.get(environment.apiUrl+`/cert/request/sent/view`);
  }

  accept(id: number):Observable<any> {
    return this.http.put(environment.apiUrl+`/cert/request/accept/${id}`, null);
  }

  reject(id: number, reason: Reason):Observable<any> {
    return this.http.put(environment.apiUrl+`/cert/request/reject/${id}`, reason);
  }
}
