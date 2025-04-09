import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenDecoderService {
  private usernameSubject = new BehaviorSubject<string>('');
  private tokenSubject = new BehaviorSubject<string | null>(localStorage.getItem('jwt'));
  username$ = this.usernameSubject.asObservable();
  token$ = this.tokenSubject.asObservable();

  constructor() {
    const token = localStorage.getItem('jwt');
    if (token) {
      this.setUsernameFromToken(token);
    }
  }

  setToken(token: string): void {
    localStorage.setItem('jwt', token);
    this.tokenSubject.next(token);
    this.setUsernameFromToken(token);
  }

  // Set the username from token
  setUsernameFromToken(token: string): void {
    try {
      const payload = this.decodePayload(token);
      const email = payload?.sub || '';
      const username = email.split('@')[0]; 
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
    if (payload) {
      return payload.type || null;
    }
    return null;
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
    this.tokenSubject.next(null);
    this.usernameSubject.next('');
  }
}
