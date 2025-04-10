import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenDecoderService {
  private usernameSubject = new BehaviorSubject<string>('');
  private tokenSubject = new BehaviorSubject<string | null>(localStorage.getItem('jwt'));
  private userIdSubject = new BehaviorSubject<number | null>(null);

  username$ = this.usernameSubject.asObservable();
  token$ = this.tokenSubject.asObservable();
  userId$ = this.userIdSubject.asObservable();

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

  setUsernameFromToken(token: string): void {
    try {
      const payload = this.decodePayload(token);
      const email = payload?.sub || '';
      const username = email.split('@')[0];
      this.usernameSubject.next(username);

      const id = payload?.id ?? null;
      this.userIdSubject.next(id);
    } catch (e) {
      console.error('Failed to decode token:', e);
      this.usernameSubject.next('');
      this.userIdSubject.next(null);
    }
  }

  decodePayload(token: string): any | null {
    try {
      const payloadBase64 = token.split('.')[1];
      const fixedBase64 = this.base64UrlFix(payloadBase64);
      const payloadJson = atob(fixedBase64);
      return JSON.parse(payloadJson);
    } catch (e) {
      console.error('Invalid token format', e);
      return null;
    }
  }

  private base64UrlFix(input: string): string {
    const base64 = input.replace(/-/g, '+').replace(/_/g, '/');
    const pad = '='.repeat((4 - (base64.length % 4)) % 4);
    return base64 + pad;
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
    const exp = this.getTokenExpiration(token);
    return exp ? exp.getTime() < Date.now() : true;
  }

  getPayload(token: string): any | null {
    return this.decodePayload(token);
  }

  getUserId(token: string): number | null {
    const payload = this.decodePayload(token);
    return payload?.id ?? null;
  }

  logout(): void {
    localStorage.removeItem('jwt');
    this.tokenSubject.next(null);
    this.usernameSubject.next('');
    this.userIdSubject.next(null);
  }
}
