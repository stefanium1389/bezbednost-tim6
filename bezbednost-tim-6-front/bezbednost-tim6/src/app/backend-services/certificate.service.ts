import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Reason } from '../view-received-requests/view-received-requests.component';
import { RequestCertificateDTO } from '../dtos/RequestCertificateDTO';
import { CertificateDTO } from '../view-all-certs/view-all-certs.component';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  
  

  constructor(private http: HttpClient) { }

  checkIsValid(serialNumber: string):Observable<any>{
    return this.http.get(environment.apiUrl+`/cert/isValid/${serialNumber}`);
  }

  checkIsValidFIle(formData: FormData) {
    return this.http.post(environment.apiUrl+'/cert/isValidFile', formData);
  }

  getAllCertificates():Observable<Array<CertificateDTO>>{
    return this.http.get<Array<CertificateDTO>>(environment.apiUrl+'/cert/getAll');
  }

  downloadFile(serialNumber: number): Observable<Blob> {
    const options = { responseType: 'blob' as 'json' };
    return this.http.get<Blob>(environment.apiUrl+`/cert/download/${serialNumber}`, options);
  }

  revoke(id: number, reason: Reason):Observable<any> {
    return this.http.put(environment.apiUrl+`/cert/revoke/${id}`, reason);
  }

  request(dto: RequestCertificateDTO): Observable<any> {
    return this.http.post(environment.apiUrl+'/cert/request', dto);
  }
  
}
