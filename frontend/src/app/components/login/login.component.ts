import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
   errorMessage: string ='';
     constructor(private http: HttpClient, private router: Router) {}

       goToCreateAccount(){
         this.router.navigate(['/createAccount'])
       }

  onLogin() {
      // Add your authentication logic here
      console.log('Email:', this.email);
      console.log('Password:', this.password);
    }
}
8
