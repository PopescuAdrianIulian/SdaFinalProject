import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { NgIf } from '@angular/common';
import { UserService } from '@service/user.service';
import { TokenDecoderService } from '@service/token.service';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
   errorMessage: string ='';

     constructor(private service: UserService, private router: Router, private tokenDecoder: TokenDecoderService) {}

       goToCreateAccount(){
         this.router.navigate(['/createAccount'])
       }

onLogin() {
  const authRequest = {
    email: this.email,
    password: this.password
  };

  this.service.login(authRequest).subscribe({
    next: (response) => {
      const token = response.token;

      if (token) {
        localStorage.setItem('jwt', token);

        // Use the decoder service
        const payload = this.tokenDecoder.getPayload(token);
        if (payload) {
          console.log('JWT Payload:', payload);
          console.log('Email (sub):', this.tokenDecoder.getUserEmail(token));
          console.log('User Type:', this.tokenDecoder.getUserType(token));
          console.log('Issued At:', new Date(payload.iat * 1000));
          console.log('Expires At:', this.tokenDecoder.getTokenExpiration(token));

          const userType = this.tokenDecoder.getUserType(token);
          if (userType === 'ADMIN') {
            this.router.navigate(['/admin/dashboard']);
          }

           if (userType === 'USER') {
             this.router.navigate(['/sendParcel']);
                   }
           if (userType === 'COURIER') {
             this.router.navigate(['/courier/dashboard']);
                          }
        } else {
          console.error('Token decoding failed.');
          this.errorMessage = 'Failed to decode token.';
        }
      } else {
        this.errorMessage = 'No token received';
      }
    },
    error: (error) => {
      console.error('Login failed:', error);
      this.errorMessage = 'Invalid email or password.';
    }
  });
}



}
