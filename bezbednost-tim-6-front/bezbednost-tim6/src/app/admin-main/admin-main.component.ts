import { Component, OnInit } from '@angular/core';
import { JwtService } from '../jwt.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-admin-main',
  templateUrl: './admin-main.component.html',
  styleUrls: ['./admin-main.component.css']
})
export class AdminMainComponent implements OnInit {

  constructor(private jwtService: JwtService,private router: Router) { }

  ngOnInit(): void {
  }
  logout():void{
    this.jwtService.logout();
    this.router.navigate(['']);
  }

}
