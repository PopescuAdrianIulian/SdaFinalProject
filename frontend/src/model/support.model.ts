export type TicketStatus = 'OPEN' | 'PROCESSING' | 'RESOLVED';

export interface SupportTicketRequest {
  title: string;
  description: string;
  email: string;
  createdAt: string;
  ticketStatus: TicketStatus;
  handlingHistory: Record<string, TicketStatus>;
}

export interface SupportTicketResponse extends SupportTicketRequest {
  id: number;
  assignee: string;
  creator: string;
}
