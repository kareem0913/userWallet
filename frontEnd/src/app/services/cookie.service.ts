import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CookieService {

  constructor() { }

  get(name: string) {
    const cookies = `; ${document.cookie}`;
    const cookieIndex = cookies.indexOf(`; ${name}=`);
    if (cookieIndex !== -1) {
      const start = cookieIndex + name.length + 3;
      let end = cookies.indexOf(";", start);
      if (end === -1) {
        end = cookies.length;
      }
      return cookies.substring(start, end);
    }
    return null; 
  }

  set(name: string, value: string, days: number) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = `${name}=${value};expires=${date.toUTCString()};path=/`;
  }
  
  remove(name: string) {
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
  }

}
