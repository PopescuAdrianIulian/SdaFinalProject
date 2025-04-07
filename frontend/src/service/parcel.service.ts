import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ParcelRequest, ParcelResponse} from "../model/parcel.model";

@Injectable({
  providedIn: 'root'
})
export class ParcelService {
  private baseUrl = 'http://localhost:8080/api/v1/parcel';

  constructor(private http: HttpClient) {
  }

  createParcel(parcelRequest: ParcelRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/createParcel`, parcelRequest);
  }

  getParcelStatusByAwb(awb: string): Observable<ParcelResponse> {
    return this.http.get<ParcelResponse>(`${this.baseUrl}/${awb}`);
  }

  getAllParcelsByUser(email: string): Observable<ParcelResponse[]> {
    return this.http.get<ParcelResponse[]>(`${this.baseUrl}/allParcels/${email}`);
  }
}
