import {Component, OnInit} from '@angular/core';
import {PackageStatus, ParcelResponse} from "../../../../model/parcel.model";
import {CommonModule, NgClass, SlicePipe} from "@angular/common";
import {ParcelService} from "@service/parcel.service";
import {TokenDecoderService} from "@service/token.service";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-find-parcels',
  standalone: true,
  imports: [
    NgClass,
    SlicePipe,
    CommonModule,
    FormsModule
  ],
  templateUrl: './find-parcels.component.html',
  styleUrl: './find-parcels.component.css'
})
export class FindParcelsComponent implements OnInit {
  parcels: ParcelResponse[] = [];
  loading = true;
  error = '';
  expandedParcelIndex: number | null = null;
  token: string | null = localStorage.getItem('jwt');
  email = '';
  selectedSort: string = 'createdDesc';

  constructor(
    private parcelService: ParcelService,
    private tokenDecoder: TokenDecoderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.token) {
      const userEmail = this.tokenDecoder.getUserEmail(this.token);
      this.email = userEmail ? userEmail : '';
    } else {
      console.warn('No token found in localStorage.');
    }

    this.loadParcels();
  }

  loadParcels(): void {
    this.loading = true;
    this.parcelService.getAllParcelsByUser(this.email).subscribe({
      next: (data) => {
        this.parcels = data;
        this.sortParcels();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load parcels. Please try again.';
        console.error('Error loading parcels:', err);
        this.loading = false;
      }
    });
  }

  toggleParcelDetails(index: number): void {
    this.expandedParcelIndex = this.expandedParcelIndex === index ? null : index;
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
  }

  getCurrentStatus(statusHistory: Record<string, PackageStatus>): PackageStatus {
    const timestamps = Object.keys(statusHistory).sort((a, b) =>
      new Date(b).getTime() - new Date(a).getTime()
    );
    return timestamps.length > 0 ? statusHistory[timestamps[0]] : 'OPEN';
  }

  getStatusClass(status: PackageStatus): string {
    switch (status) {
      case 'OPEN':
        return 'status-open';
      case 'IN_TRANSIT':
        return 'status-transit';
      case 'DELIVERED':
        return 'status-delivered';
      case 'CANCELED':
        return 'status-canceled';
      default:
        return '';
    }
  }

  sortParcels(): void {
    switch (this.selectedSort) {
      case 'createdAsc':
        this.parcels.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
        break;
      case 'createdDesc':
        this.parcels.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        break;
      case 'updatedAsc':
        this.parcels.sort((a, b) => new Date(a.updatedAt).getTime() - new Date(b.updatedAt).getTime());
        break;
      case 'updatedDesc':
        this.parcels.sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime());
        break;
    }
  }

  goToSendParcel() {
    this.router.navigate(['/sendParcel']);
  }

  protected readonly Object = Object;
}
