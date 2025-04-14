package com.example.orderservice.repository;

import com.example.orderservice.entity.Parcel;
import com.example.orderservice.enums.PackageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    Optional<Parcel> findParcelByAwb(String awb);

    List<Parcel> findParcelByDestinationEmail(String destinationEmail);

    long count();

    long countByStatus(PackageStatus status);

    @Query("SELECT COUNT(p) FROM Parcel p WHERE p.status = 'DELIVERED'")
    long countDelivered();

    @Query("SELECT DATE(p.createdAt), COUNT(p) FROM Parcel p GROUP BY DATE(p.createdAt)")
    List<Object[]> countParcelsPerDay();

    @Query("SELECT COUNT(p) FROM Parcel p WHERE p.createdAt >= :date")
    long countParcelsSince(@Param("date") LocalDateTime date);

    @Query(value = "SELECT AVG(TIMESTAMPDIFF(HOUR, created_at, updated_at)) FROM parcel WHERE delivered = true", nativeQuery = true)
    Double averageDeliveryTime();

    @Query("SELECT AVG(p.price) FROM Parcel p")
    Double averageParcelPrice();

    @Query("SELECT SUM(p.price) FROM Parcel p")
    Double totalRevenue();

    @Query("SELECT FUNCTION('MONTH', p.createdAt), SUM(p.price) " +
            "FROM Parcel p GROUP BY FUNCTION('MONTH', p.createdAt) ORDER BY FUNCTION('MONTH', p.createdAt)")
    List<Object[]> totalMoneyPerMonth();

}
