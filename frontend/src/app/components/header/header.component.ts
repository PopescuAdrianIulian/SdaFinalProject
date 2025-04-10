import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TokenDecoderService} from '@service/token.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgIf],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  username: string = '';
  isLoggedIn: boolean = false;

  userType: string | null = null;

  constructor(
    private router: Router,
    private tokenDecoder: TokenDecoderService
  ) {
  }

  ngOnInit(): void {
    this.tokenDecoder.username$.subscribe(name => {
      this.username = name;
      this.isLoggedIn = !!name;
    });
    const token = localStorage.getItem('jwt');
    if (token) {
      this.userType = this.tokenDecoder.getUserType(token);
      console.log(this.userType)
    }
  }


  goToHome() {
    this.router.navigate(['/home']);
  }

  goToMyParcels() {
    if (this.isLoggedIn) {
      this.router.navigate(['/myParcels']);
    } else {
      this.goToLogin();
    }
  }

  goToSystem() {
    if (this.isLoggedIn) {
      this.router.navigate(['/system']);
    } else {
      this.goToLogin();
    }
  }

  goToSendParcel() {
    if (this.isLoggedIn) {
      this.router.navigate(['/sendParcel']);
    } else {
      this.goToLogin();
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.tokenDecoder.logout();
    this.userType = null;
    this.isLoggedIn = false;
    this.router.navigate(['']);
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }


  goToSupport() {
    this.router.navigate(['/support']);

  }

  goToProducts() {

  }
}
