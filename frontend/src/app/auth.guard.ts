import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenDecoderService } from '../service/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private tokenDecoderService: TokenDecoderService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    const token = this.tokenDecoderService.getToken();
    const expectedRole = route.data['expectedRole'];
    const userRole = token ? this.tokenDecoderService.getUserType(token) : null;

    // If no token or role doesn't match the expected, redirect to login
    if (!token || userRole !== expectedRole) {
      this.router.navigate(['/home']);
      return false;
    }

    return true;
  }
}
