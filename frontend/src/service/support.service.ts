import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {SupportTicketRequest, TicketStatus} from "../model/support.model";

@Injectable({
  providedIn: 'root'
})
export class SupportTicketService {
  private apiUrl = 'http://localhost:8080/api/v1/support';

  constructor(private http: HttpClient) {}

  createSupportTicket(ticketRequest: SupportTicketRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, ticketRequest);
  }

  updateSupportTicket(id: number, newStatus: TicketStatus): Observable<any> {
    return this.http.post(`${this.apiUrl}/${id}/${newStatus}`, {});
  }
}
