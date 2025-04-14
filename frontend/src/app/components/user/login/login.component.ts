import {Component} from "@angular/core";
import {NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {UserService} from "@service/user.service";
import {Router} from "@angular/router";
import {TokenDecoderService} from "@service/token.service";


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
  errorMessage: string = '';

  constructor(
    private service: UserService,
    private router: Router,
    private tokenDecoder: TokenDecoderService
  ) {
  }

  goToCreateAccount(): void {
    this.router.navigate(['/createAccount']);
  }

  onLogin(): void {
    const authRequest = {
      email: this.email,
      password: this.password
    };

    this.service.login(authRequest).subscribe({
      next: (response) => {
        const token = response.token;

        if (token) {
          localStorage.setItem('jwt', token);
          this.tokenDecoder.setUsernameFromToken(token);

          const payload = this.tokenDecoder.getPayload(token);

          if (payload) {
            console.log('JWT Payload:', payload);
            console.log('Email (sub):', payload.sub);
            console.log('User Type:', payload.type);
            console.log('Issued At:', new Date(payload.iat * 1000));
            console.log('Expires At:', new Date(payload.exp * 1000));

            switch (payload.type) {
              case 'ADMIN':
                this.router.navigate(['/tickets']);
                break;
              case 'USER':
                this.router.navigate(['/home']);
                break;
              case 'COURIER':
                this.router.navigate(['/dashboard']);
                break;
              default:
                this.router.navigate(['/home']);
                break;
            }
          } else {
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
