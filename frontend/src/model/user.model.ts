export interface AuthRequest {
  email: string;
  password: string;
}

export interface AccountRequest {
  name: string;
  email: string;
  phone: string;
  address: string;
}

export interface User {
  id?: number;
  name: string;
  email: string;
  phone: string;
  address: string;
  password: string;
  userType?: string;
}
