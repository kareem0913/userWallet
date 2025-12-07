import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CookieService } from 'src/app/services/cookie.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private httpClient: HttpClient,
    private cookieService: CookieService
  ) {}

  register(body: any): Observable<any> {
    return this.httpClient.post(`${environment.url}/auth/register`, body);
  }

  login(body: any): Observable<any> {
    return this.httpClient.post(`${environment.url}/auth/login`, body);
  }

  logout(): Observable<any> {
    return this.httpClient.get(`${environment.url}/auth/logout`, {
      headers: {
        Authorization: `Bearer ${this.cookieService.get('token')}`,
      },
    });
  }
}
