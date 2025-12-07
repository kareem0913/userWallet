import { Injectable } from '@angular/core';
import { CookieService } from '../../services/cookie.service';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private cookieService: CookieService, private httpClient: HttpClient) { }

  public userProfile(): Observable<any>{
    return this.httpClient.get(`${environment.url}/users?action=profile`, {
      headers: {
        'Authorization': `Bearer ${this.cookieService.get('token')}`
      }
    });
  }

  public allUsers(): Observable<any>{
    return this.httpClient.get(`${environment.url}/users?action=allUsers`, {
      headers: {
        'Authorization': `Bearer ${this.cookieService.get('token')}`
      }
    });
  }
  
}
