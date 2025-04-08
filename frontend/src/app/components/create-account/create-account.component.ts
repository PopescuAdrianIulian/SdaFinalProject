import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '@service/user.service';
import { User } from '../../../model/user.model';

@Component({
  selector: 'app-create-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-account.component.html',
  styleUrl: './create-account.component.css'
})
export class CreateAccountComponent {
  name: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  phone: string = '0743455172';
  address: string = 'adress';
  errorMessage: string = '';

  constructor(private service: UserService, private router: Router) {}

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  onCreateAccount(): void {
     console.log('Name:', this.name);
      console.log('Email:', this.email);
      console.log('Password:', this.password);
      console.log('Confirm Password:', this.confirmPassword);
      console.log('Phone:', this.phone);
      console.log('Address:', this.address);
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    const user: User = {
      name: this.name,
      email: this.email,
      password: this.password,
      phone: this.phone,
      address: this.address
    };


    this.service.createAccount(user).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        console.error('Create account failed:', err);
        this.errorMessage = 'Account creation failed. Try again.';
      }
    });
  }
}
