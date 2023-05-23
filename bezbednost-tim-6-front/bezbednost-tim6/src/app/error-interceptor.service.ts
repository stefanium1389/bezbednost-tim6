import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { LoginService } from './backend-services/login.service';
import { JwtService } from './jwt.service';
import { LoginResponse } from './dtos/LoginDtos';
import { error } from 'ajv/dist/vocabularies/applicator/dependencies';

@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptorService implements HttpInterceptor {

  constructor(private loginService: LoginService, private jwtService: JwtService) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      retry(3),
      catchError((err: HttpErrorResponse) => {
        if(err.status == 403){
          const dto:LoginResponse = {accessToken:this.jwtService.getAccessToken()!, refreshToken: this.jwtService.getRefreshToken()!}
          this.loginService.refreshToken(dto).subscribe({
            next: (result) => {
              this.jwtService.setAccessToken(result.message);
            },
            error: (error) => {
              alert("session expired, login again")
              this.jwtService.logout();
            }
          })
        }
        return throwError(err);
      })
    )
  }
}
