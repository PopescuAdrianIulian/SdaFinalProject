export type TicketStatus = 'OPEN' | 'RESOLVED';

export interface SupportTicketRequest {
  title: string;
  description: string;
  email: string;

}

export interface SupportTicketResponse extends SupportTicketRequest {
  id: string;
  title: string;
  description: string;
  email: string;
  createdAt: string;
  ticketStatus: TicketStatus;
  handlingHistory: Record<string, TicketStatus>;
  messageHistory: Record<string, string>;
}
