import {Component} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ParcelRequest, Size} from "../../../model/parcel.model";
import {CommonModule} from "@angular/common";
import {ParcelService} from "@service/parcel.service";

@Component({
  selector: 'app-send-parcel',
  standalone: true,
  imports: [CommonModule,
    ReactiveFormsModule, FormsModule],
  templateUrl: './send-parcel.component.html',
  styleUrl: './send-parcel.component.css'
})
export class SendParcelComponent {
  currentStep = 1;
  isLoading = false;

  parcel: ParcelRequest = {
    size: 'SMALL',
    weight: 1,
    destinationAddress: '',
    destinationContact: '',
    destinationPhone: '',
    destinationEmail: '',
    fragile: false,
    price: 9.99,
    email: ''
  };

  firstName = '';
  lastName = '';
  addressLine = '';
  city = '';
  country = '';

  sizePrice = {
    'SMALL': 9.99,
    'MEDIUM': 14.99,
    'LARGE': 19.99
  };

  constructor(private service: ParcelService) {
  }

  nextStep() {
    if (this.currentStep === 2) {
      this.parcel.destinationContact = `${this.firstName} ${this.lastName}`;

      this.parcel.destinationAddress = `${this.addressLine}, ${this.city}, ${this.country}`;
    }

    this.currentStep++;
  }

  previousStep() {
    this.currentStep--;
  }

  updateSize(size: Size) {
    this.parcel.size = size;
    this.parcel.price = this.sizePrice[size];

    if (this.parcel.fragile) {
      this.parcel.price += 5;
    }
  }

  toggleFragile() {
    this.parcel.fragile = !this.parcel.fragile;

    const basePrice = this.sizePrice[this.parcel.size];
    this.parcel.price = this.parcel.fragile ? basePrice + 5 : basePrice;
  }

  submitParcel() {
    this.isLoading = true;

    this.service.createParcel(this.parcel).subscribe({
      next: (response) => {
        console.log('Parcel submitted:', response);
        alert('Parcel submitted successfully!');
        this.resetForm();
      },
      error: (error) => {
        console.error('Error submitting parcel:', error);
        alert('Failed to submit parcel. Please try again.');
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  resetForm() {
    this.currentStep = 1;
    this.parcel = {
      size: 'SMALL',
      weight: 1,
      destinationAddress: '',
      destinationContact: '',
      destinationPhone: '',
      destinationEmail: '',
      fragile: false,
      price: 9.99,
      email: ''
    };
    this.firstName = '';
    this.lastName = '';
    this.addressLine = '';
    this.city = '';
    this.country = '';
  }
}
