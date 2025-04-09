export type Size = 'SMALL' | 'MEDIUM' | 'LARGE';
export type PackageStatus = 'OPEN' | 'IN_TRANSIT' | 'DELIVERED' | 'CANCELED';

export interface ParcelRequest {
  size: Size;
  weight: number;
  destinationAddress: string;
  destinationContact: string;
  destinationPhone: string;
  destinationEmail: string;
  fragile: boolean;
  price: number;
  email: string;
}

export interface ParcelResponse {
  size: Size;
  weight: number;
  destinationAddress: string;
  destinationContact: string;
  destinationPhone: string;
  destinationEmail: string;
  fragile: boolean;
  createdAt: string;
  updatedAt: string;
  delivered: boolean;
  email: string;
  awb: string;
  price: number;
  statusHistory: Record<string, PackageStatus>;
}
