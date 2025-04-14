import { Component, OnInit } from '@angular/core';
import { CommonModule, KeyValue } from '@angular/common';
import { TokenDecoderService } from '@service/token.service';
import { SupportTicketService } from '@service/support.service';
import { SupportTicketResponse, TicketStatus } from '../../../../model/support.model';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-support-tickets',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './support-tickets.component.html',
  styleUrls: ['./support-tickets.component.scss']
})
export class SupportTicketsComponent implements OnInit {
  openTickets: SupportTicketResponse[] = [];
  myTickets: SupportTicketResponse[] = [];
  loadingOpen = false;
  loadingMine = false;
  currentUserId: number | null = null;
  currentUserRole: string | null = '';
  currentUserEmail: string | null = '';
  selectedTicket: SupportTicketResponse | null = null;
  newMessage: string = '';
  ticketStatuses: TicketStatus[] = ['OPEN', 'RESOLVED', 'PROCESSING'];
  adminAssignedTickets: SupportTicketResponse[] = [];


  constructor(
    private router: Router,
    private supportTicketService: SupportTicketService,
    private tokenDecode: TokenDecoderService
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (token) {
      this.currentUserId = this.tokenDecode.getUserId(token);
      this.currentUserRole = this.tokenDecode.getUserType(token);
      this.currentUserEmail = this.tokenDecode.getUserEmail(token);
    }

    if (this.currentUserRole === 'ADMIN') {
      this.loadAdminTickets();
    }

    if (this.currentUserRole === 'USER') {
      this.loadUserTicketsByEmail();
    }
  }

  loadAdminTickets(): void {
    if (this.currentUserId == null) return;

    this.loadingOpen = true;

    this.supportTicketService.getOpenTickets().subscribe({
      next: (tickets) => {
        this.openTickets = tickets;
      },
      error: () => {
        this.loadingOpen = false;
      }
    });

    this.supportTicketService.getMyTickets(this.currentUserId).subscribe({
      next: (tickets) => {
        this.adminAssignedTickets = tickets;
        this.loadingOpen = false;
      },
      error: () => {
        this.loadingOpen = false;
      }
    });
  }

  loadUserTickets(): void {

  }

  loadUserTicketsByEmail(): void {
    if (!this.currentUserEmail) return;

    this.loadingMine = true;
    this.supportTicketService.getAllByEmail(this.currentUserEmail).subscribe({
      next: (tickets) => {
        this.myTickets = tickets;
        this.loadingMine = false;
      },
      error: () => {
        this.loadingMine = false;
      }
    });
  }

  selectTicket(ticket: SupportTicketResponse): void {
    this.selectedTicket = ticket;
    this.newMessage = '';
  }
  submitMessage(): void {
    if (this.selectedTicket && this.newMessage.trim()) {
      this.supportTicketService
        .addMessageForSupportTicket(String(this.selectedTicket.id), this.newMessage)
        .subscribe({
          next: () => {
            this.reloadSelectedTicket();
            this.newMessage = '';
          },
          error: (err: any) => {
            console.error('Error sending message', err);
          }
        });
    }
  }

  reloadSelectedTicket(): void {
    if (this.selectedTicket) {
      this.supportTicketService.getTicketById(this.selectedTicket.id).subscribe({
        next: (ticket) => {
          this.selectedTicket = ticket;
        }
      });
    }
  }

  updateTicketStatus(ticket: SupportTicketResponse, newStatus: TicketStatus): void {
    if (this.currentUserId === null) return;

    this.supportTicketService.updateSupportTicketStatus(ticket.id, newStatus, this.currentUserId).subscribe({
      next: () => {
        this.reloadSelectedTicket();
      }
    });
  }

  sortByTimestamp(a: KeyValue<string, string>, b: KeyValue<string, string>): number {
    return a.key.localeCompare(b.key);
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString();
  }

  isAdmin(): boolean {
    return this.currentUserRole === 'ADMIN';
  }

  isUser(): boolean {
    return this.currentUserRole === 'USER';
  }

  onAddTicket(): void {
    this.router.navigate(['/add-support']);
  }
}
