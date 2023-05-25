import { Component, OnInit } from '@angular/core';
import { JwtService } from '../jwt.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-user-main',
  templateUrl: './user-main.component.html',
  styleUrls: ['./user-main.component.css']
})
export class UserMainComponent implements OnInit {

  constructor(private jwtService: JwtService,private router: Router) { }

  ngOnInit(): void {
  }
  logout():void{
    this.jwtService.logout();
    this.router.navigate(['']);
  }
}
