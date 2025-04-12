import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {CommonModule, KeyValuePipe} from "@angular/common";
import {ParcelResponse} from "../../../model/parcel.model";
import {Router} from "@angular/router";
import {ParcelService} from "@service/parcel.service";


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    FormsModule,
    KeyValuePipe,
    CommonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  enteredAWB: string = '';

  parcel: ParcelResponse | null = null;

  constructor(private router: Router, private parcelService: ParcelService) {
  }

  onSubmit() {
    this.parcelService.getParcelStatusByAwb(this.enteredAWB)
      .subscribe(response => {
          this.parcel = response;
        },
        error => {
          console.error('AWB not found or error occurred', error);
          this.parcel = null;
        });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
  }

  goToSupport() {
    this.router.navigate(['/tickets']);

  }

  goToReturnParcel() {
    this.router.navigate(['/returnParcel']);

  }

  goToInfo() {
    this.router.navigate(['/info']);
  }

  goToSendParcel() {
    this.router.navigate(['/sendParcel']);

  }
}
