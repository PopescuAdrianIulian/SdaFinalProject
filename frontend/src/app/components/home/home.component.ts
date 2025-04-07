import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {ParcelService} from "../../../service/parcel.service";
import {ParcelResponse} from "../../../model/parcel.model";
import {CommonModule, KeyValuePipe} from "@angular/common";
import {pipe} from "rxjs";

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

  protected readonly pipe = pipe;
}
