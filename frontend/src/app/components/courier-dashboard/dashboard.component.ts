import { Component, OnInit } from '@angular/core';
import { PackageStatus, ParcelResponse } from "../../../model/parcel.model";
import { Router } from "@angular/router";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { TrackingService } from "@service/tracking.service";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'] // Correct property name for styles
})
export class DashboardComponent implements OnInit {

  parcels: ParcelResponse[] = [];
  loading = true;
  error = '';
  expandedParcelIndex: number | null = null;
  selectedSort: string = 'createdDesc';

  packageStatuses: PackageStatus[] = ['OPEN', 'IN_TRANSIT', 'DELIVERED', 'CANCELED'];

  constructor(
    private trackingService: TrackingService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadParcels();
  }

  loadParcels(): void {
    this.loading = true;
    this.trackingService.getAllUndeliveredParcels().subscribe({
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
    return timestamps.length > 0 ? statusHistory[timestamps[0]] : 'OPEN' as PackageStatus;
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

  protected readonly Object = Object;

  onStatusChange(awb: string, newStatus: PackageStatus): void {
    const confirmation = confirm(`Are you sure you want to change the status of parcel ${awb} to ${newStatus}?`);

    if (confirmation) {
      this.trackingService.changeDeliveryStatus(awb, newStatus).subscribe({
        next: response => {
          console.log('Status updated', response);
          this.loadParcels();
        },
        error: err => {
          console.error('Error updating status', err);
        }
      });
    } else {
      console.log('Status change canceled');
    }
  }

  changeParcelStatus(awb: string): void {
    const newStatusInput = prompt(`Enter new status for parcel ${awb} (available: ${this.packageStatuses.join(', ')})`);
    if (newStatusInput && this.packageStatuses.includes(newStatusInput as PackageStatus)) {
      this.onStatusChange(awb, newStatusInput as PackageStatus);
    } else {
      alert('Invalid status provided.');
    }
  }


  goToSendParcel(): void {
    this.router.navigate(['/send-parcel']);
  }
}
