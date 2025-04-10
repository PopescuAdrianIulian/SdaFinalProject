export interface SupportTicketRequest {
  title: string;
  description: string;
  email: string;
}

export type TicketStatus = 'OPEN' | 'RESOLVED';

