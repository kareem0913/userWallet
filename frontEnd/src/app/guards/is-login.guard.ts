import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { CookieService } from '../services/cookie.service';

@Injectable({
  providedIn: 'root',
})
export class IsLoginGuard implements CanActivate {
  constructor(
    private CookieService: CookieService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.checkLogin();
  }

  private checkLogin(): boolean | UrlTree {
    const token = this.CookieService.get('token');
    if (!token) {
      return true;
    } else {
      return this.router.createUrlTree(['/']);
    }
  }
}
