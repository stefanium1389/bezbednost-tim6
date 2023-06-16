import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  showNavbar: boolean = true;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        
        // Gledamo na kojoj smo putanji i prikazujemo navbar u skladu sa tim
        const navigationEndEvent = event as NavigationEnd;
        const url = navigationEndEvent.url;
        if (url === '' || url === '/' || url ==='/verify-code') {
          this.showNavbar = false;
        } else {
          this.showNavbar = !url.includes('/register') && !url.includes('/main') &&
          !url.includes('/reset-password') && !url.includes('/renew-password') &&
          !url.includes('/sms-validation') && !url.includes('/activate')
          ;
        }
      }
    });
  }
}
