import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenDecoderService } from '@service/token.service';
import { NgIf } from '@angular/common';

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

  constructor(
    private router: Router,
    private tokenDecoder: TokenDecoderService
  ) {}

  ngOnInit(): void {
    this.tokenDecoder.username$.subscribe(name => {
      this.username = name;
      this.isLoggedIn = !!name; // set true if username exists
    });
  }

  goToHome() {
    this.router.navigate(['/home']);
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.tokenDecoder.logout();
    this.isLoggedIn = false;
    this.router.navigate(['']);
  }
}

