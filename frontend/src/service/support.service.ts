import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SupportTicketRequest, SupportTicketResponse, TicketStatus} from '../model/support.model';

@Injectable({
  providedIn: 'root'
})
export class SupportTicketService {
  private apiUrl = 'http://localhost:8080/api/v1/support';

  constructor(private http: HttpClient) {
  }

  createSupportTicket(ticketRequest: SupportTicketRequest): Observable<SupportTicketResponse> {
    return this.http.post<SupportTicketResponse>(`${this.apiUrl}/create`, ticketRequest);
  }



  updateSupportTicketStatus(id: string, newStatus: TicketStatus,adminId:number): Observable<SupportTicketResponse> {
    return this.http.post<SupportTicketResponse>(`${this.apiUrl}/${id}/${adminId}/${newStatus}`, {});
  }

  getOpenTickets(): Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/openTickets`);
  }

  getMyTickets(userId: number): Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/myTickets/${userId}`);
  }

  getTicketById(ticketId: string): Observable<SupportTicketResponse> {
    return this.http.get<SupportTicketResponse>(`${this.apiUrl}/supportTicket/${ticketId}`);
  }

  addMessageForSupportTicket(ticketId: string, message: string) {
    return this.http.post(`${this.apiUrl}/message/${ticketId}`, message);
  }

  getAllByEmail(email: string ):Observable<SupportTicketResponse[]> {
    return this.http.get<SupportTicketResponse[]>(`${this.apiUrl}/getAll/${email}`)
  }
}
