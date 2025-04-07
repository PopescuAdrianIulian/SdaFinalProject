import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AccountRequest, AuthRequest, User} from "../model/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/api/v1/user';

  constructor(private http: HttpClient) {
  }

  login(authRequest: AuthRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, authRequest);
  }

  createAccount(user: User): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, user);
  }

  updatePassword(authRequest: AuthRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/update/password`, authRequest);
  }

  updateAccount(accountRequest: AccountRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/update/accountDetails`, accountRequest);
  }
}
