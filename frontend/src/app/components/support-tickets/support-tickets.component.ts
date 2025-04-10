import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TokenDecoderService } from '@service/token.service';
import {SupportTicketService} from "@service/support.service";
import { SupportTicketResponse } from '../../../model/support.model';

@Component({
  selector: 'app-support-tickets',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './support-tickets.component.html',
  styleUrls: ['./support-tickets.component.scss']
})
export class SupportTicketsComponent implements OnInit {
  openTickets: SupportTicketResponse[] = [];
  myTickets: SupportTicketResponse[] = [];

  loadingOpen = false;
  loadingMine = false;

  currentUserId: number | null = null;

  constructor(
    private supportTicketService: SupportTicketService,
    private tokenDecode: TokenDecoderService
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (token) {
      this.currentUserId = this.tokenDecode.getUserId(token);
    }

    this.loadOpenTickets();

    if (this.currentUserId !== null) {
      this.loadMyTickets();
    } else {
      console.warn('User ID not found in token');
    }
  }

  loadOpenTickets(): void {
    this.loadingOpen = true;
    this.supportTicketService.getOpenTickets().subscribe({
      next: (tickets: SupportTicketResponse[]) => {
        console.log('Open tickets:', tickets);
        this.openTickets = tickets;
        this.loadingOpen = false;
      },
      error: (err: any) => {
        console.error('Error fetching open tickets', err);
        this.loadingOpen = false;
      }
    });
  }

  loadMyTickets(): void {
    if (this.currentUserId === null) return;

    this.loadingMine = true;
    this.supportTicketService.getMyTickets(this.currentUserId).subscribe({
      next: (tickets: SupportTicketResponse[]) => {
        this.myTickets = tickets;
        this.loadingMine = false;
      },
      error: (err: any) => {
        console.error('Error fetching my tickets', err);
        this.loadingMine = false;
      }
    });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString();
  }

  getLastStatus(history: { [timestamp: string]: string }): string {
   if (!history || Object.keys(history).length === 0) return 'No History';

     const latestTimestamp = Object.keys(history).sort().reverse()[0];
     const currentStatus = history[latestTimestamp];

     return currentStatus || 'Unknown';
  }
}
