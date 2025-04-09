import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PackageStatus, ParcelResponse} from "../model/parcel.model";


@Injectable({
  providedIn: 'root'
})
export class TrackingService {

  private apiUrl = 'http://localhost:8080/api/v1/tracking';

  constructor(private http: HttpClient) {
  }

  changeDeliveryStatus(awb: string, newStatus: string): Observable<ParcelResponse> {
    return this.http.post<ParcelResponse>(`${this.apiUrl}/status/${awb}/${newStatus}`, {});
  }

  setParcelAsDelivered(awb: string): Observable<ParcelResponse> {
    return this.http.post<ParcelResponse>(`${this.apiUrl}/delivered/${awb}`, {});
  }

  getAllParcels(): Observable<ParcelResponse[]> {
    return this.http.get<ParcelResponse[]>(`${this.apiUrl}/parcels`);
  }

  getAllUndeliveredParcels(): Observable<ParcelResponse[]> {
    return this.http.get<ParcelResponse[]>(`${this.apiUrl}/allParcels`);
  }
}
