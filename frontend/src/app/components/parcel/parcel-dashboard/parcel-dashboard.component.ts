import { Component, OnInit } from '@angular/core';
import { PackageStatus, ParcelResponse } from '../../../../model/parcel.model';
import { TrackingService } from '@service/tracking.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-parcel-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './parcel-dashboard.component.html',
  styleUrls: ['./parcel-dashboard.component.css']
})
export class ParcelDashboardComponent implements OnInit {
  parcels: ParcelResponse[] = [];
  loading = true;
  error = '';
  expandedParcelIndex: number | null = null;
  selectedSort: string = 'createdDesc';
  searchTerm: string = '';

  packageStatuses: PackageStatus[] = ['OPEN', 'IN_TRANSIT', 'DELIVERED', 'CANCELED'];

  constructor(private trackingService: TrackingService) {}

  ngOnInit(): void {
    this.loadParcels();
  }

  loadParcels(): void {
    this.loading = true;
    this.error = '';
    this.trackingService.getAllParcels().subscribe({
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

  get filteredParcels(): ParcelResponse[] {
    if (!this.searchTerm.trim()) return this.parcels;

    return this.parcels.filter(parcel =>
      parcel.awb.toLowerCase().includes(this.searchTerm.trim().toLowerCase())
    );
  }

  protected readonly Object = Object;
}
