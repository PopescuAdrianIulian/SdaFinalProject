import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: 'app-create-account',
  standalone: true,
  imports: [],
  templateUrl: './create-account.component.html',
  styleUrl: './create-account.component.css'
})
export class CreateAccountComponent {
    email: string = '';
    password: string = '';
    errorMessage: string ='';
   constructor(private http: HttpClient, private router: Router){}
goToLogin(){
  this.router.navigate(['/login'])
  }
}
