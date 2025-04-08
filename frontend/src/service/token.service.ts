import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenDecoderService {
  private usernameSubject = new BehaviorSubject<string>('');
  username$ = this.usernameSubject.asObservable();

  constructor() {
    // Initialize username on service load if token exists
    const token = localStorage.getItem('jwt');
    if (token) {
      this.setUsernameFromToken(token);
    }
  }

  setUsernameFromToken(token: string): void {
    try {
      const payload = this.decodePayload(token);
      const email = payload?.sub || '';
      const username = email.split('@')[0]; // Simple extraction logic
      this.usernameSubject.next(username);
    } catch (e) {
      console.error('Failed to decode token:', e);
      this.usernameSubject.next('');
    }
  }

  decodePayload(token: string): any | null {
    try {
      const payloadBase64 = token.split('.')[1];
      const payloadJson = atob(payloadBase64);
      return JSON.parse(payloadJson);
    } catch (e) {
      console.error('Invalid token format', e);
      return null;
    }
  }

  getUserType(token: string): string | null {
    const payload = this.decodePayload(token);
    return payload?.type || null;
  }
getUserEmail(token: string): string | null {
  const payload = this.decodePayload(token);
  return payload?.sub || null;
}

  getTokenExpiration(token: string): Date | null {
    const payload = this.decodePayload(token);
    return payload?.exp ? new Date(payload.exp * 1000) : null;
  }

  isTokenExpired(token: string): boolean {
    const expDate = this.getTokenExpiration(token);
    return expDate ? expDate.getTime() < Date.now() : true;
  }

getPayload(token: string): any | null {
  try {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64);
    return JSON.parse(payloadJson);
  } catch (e) {
    console.error('Failed to decode token payload:', e);
    return null;
  }
}

  logout(): void {
    localStorage.removeItem('jwt');
    this.usernameSubject.next('');
  }
}
