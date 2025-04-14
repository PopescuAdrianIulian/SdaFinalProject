package com.example.orderservice.response.parcel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelStatsResponse {
    private long totalParcels;
    private long deliveredParcels;
    private long inTransitParcels;
    private long failedParcels;
    private double successRate;
    private double averageDeliveryTime;
    private double averagePrice;
    private long totalUsers;
    private double totalRevenue;
}
