import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenDecoderService {
  constructor() {}

  getPayload(token: string): any | null {
    try {
      const payloadBase64 = token.split('.')[1];
      const payloadJson = atob(payloadBase64);
      return JSON.parse(payloadJson);
    } catch (error) {
      console.error('Invalid token format', error);
      return null;
    }
  }

  getUserEmail(token: string): string | null {
    const payload = this.getPayload(token);
    return payload?.sub || null;
  }

  getUserType(token: string): string | null {
    const payload = this.getPayload(token);
    return payload?.type || null;
  }

  getTokenExpiration(token: string): Date | null {
    const payload = this.getPayload(token);
    return payload?.exp ? new Date(payload.exp * 1000) : null;
  }

  isTokenExpired(token: string): boolean {
    const expDate = this.getTokenExpiration(token);
    return expDate ? expDate.getTime() < Date.now() : true;
  }
}
