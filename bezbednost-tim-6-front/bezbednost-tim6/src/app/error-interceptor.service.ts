import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, switchMap, tap, throwError } from 'rxjs';
import { LoginService } from './backend-services/login.service';
import { JwtService } from './jwt.service';
import { LoginResponse } from './dtos/LoginDtos';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptorService implements HttpInterceptor {

  constructor(private loginService: LoginService, private jwtService: JwtService, private router: Router) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status == 403) {
          const dto: LoginResponse = {
            accessToken: this.jwtService.getAccessToken()!,
            refreshToken: this.jwtService.getRefreshToken()!
          };
  
          return this.loginService.refreshToken(dto).pipe(
            switchMap((result) => {
              this.jwtService.setAccessToken(result.message);
              return next.handle(req);
            }),
            catchError((error) => {
              if(error.status == 510){
                alert("Session expired, please login again");
                this.jwtService.logout();
                this.router.navigate(['']);
              }
              return throwError(error);
            })
          );
        }
        return throwError(err);
      })
    );
  }
    
}
