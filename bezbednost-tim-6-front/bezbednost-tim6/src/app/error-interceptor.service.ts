import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { LoginService } from './backend-services/login.service';
import { JwtService } from './jwt.service';
import { LoginResponse } from './dtos/LoginDtos';
import { Router } from '@angular/router';
import { SuccessDTO } from './dtos/MessageDtos';

@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptorService implements HttpInterceptor {

  constructor(private loginService: LoginService, private jwtService: JwtService, private router: Router) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        if(err.status == 403){
          const dto:LoginResponse = {accessToken:this.jwtService.getAccessToken()!, refreshToken: this.jwtService.getRefreshToken()!}
          this.loginService.refreshToken(dto).subscribe({
            next: (result) => {
              this.jwtService.setAccessToken(result.message);
              next.handle(req).subscribe();
            },
            error: (error) => {
              alert("Session expired, pleaselogin again")
              //this.jwtService.logout();
              //router.navigate(['']);
            }
          })
        }
        return throwError(err);
      })
    )
  }
}
