import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupportTicketRequest, SupportTicketResponse, TicketStatus } from '../model/support.model';

@Injectable({
  providedIn: 'root'
})
export class SupportTicketService {
  private apiUrl = 'http://localhost:8080/api/v1/support';

  constructor(private http: HttpClient) {}

  createSupportTicket(ticketRequest: SupportTicketRequest): Observable<SupportTicketResponse> {
    return this.http.post<SupportTicketResponse>(`${this.apiUrl}/create`, ticketRequest);
  }

  updateSupportTicketStatus(id: number, newStatus: TicketStatus): Observable<SupportTicketResponse> {
    return this.http.post<SupportTicketResponse>(`${this.apiUrl}/${id}/${newStatus}`, {});
  }

  getOpenTickets(): Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/openTickets`);
  }

  getMyTickets(userId: number): Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/myTickets/${userId}`);
  }

  getMyOpenTickets(userId: number): Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/myOpenTickets/${userId}`);
  }

  getAllUnresolvedTickets(): Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/getAll`);
  }
}
