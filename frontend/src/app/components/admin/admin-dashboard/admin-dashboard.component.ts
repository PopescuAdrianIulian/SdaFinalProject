import {Component} from '@angular/core';
import {ParcelService} from "@service/parcel.service";
import {CommonModule, CurrencyPipe} from "@angular/common";

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CurrencyPipe,
    CommonModule
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  totalParcels: number = 0;
  deliveredParcels: number = 0;
  inTransitParcels: number = 0;
  failedParcels: number = 0;
  successRate: number = 0;
  averageDeliveryTime: number = 0;
  averagePrice: number = 0;
  totalUsers: number = 0;
  totalRevenue: number = 0;
  monthlyRevenue: { month: string; amount: number }[] = [];

  constructor(private parcelService: ParcelService) {
  }

  ngOnInit(): void {
    this.fetchParcelStats();
  }

  fetchParcelStats(): void {
    this.parcelService.getParcelStats().subscribe((data) => {
      this.totalParcels = data.totalParcels;
      this.deliveredParcels = data.deliveredParcels;
      this.inTransitParcels = data.inTransitParcels;
      this.failedParcels = data.failedParcels;
      this.successRate = data.successRate;
      this.averageDeliveryTime = data.averageDeliveryTime;
      this.averagePrice = data.averagePrice;
      this.totalUsers = data.totalUsers;
      this.totalRevenue = data.totalRevenue;
    });
  }


}
