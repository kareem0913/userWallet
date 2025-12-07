import { Injectable } from '@angular/core';
import { CookieService } from '../../services/cookie.service';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private httpClient:HttpClient, private CookieService: CookieService) { }

  public transaction(action: string): Observable<any>{
    return this.httpClient.get(
      `${environment.url}/transactions?action=${action}`,
      {
        headers: {
          Authorization: `Bearer ${this.CookieService.get('token')}`,
        },
      }
    );
  }

  public transferMoney(body: any): Observable<any>{
    return this.httpClient.post(`${environment.url}/transactions`, body, {
      headers : {
        'Authorization': `Bearer ${this.CookieService.get('token')}`,
      }
    });
  }

}
