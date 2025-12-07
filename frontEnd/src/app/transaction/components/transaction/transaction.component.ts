import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'src/app/services/cookie.service';
import { TransactionService } from '../../services/transaction.service';
import { UserService } from 'src/app/user/services/user.service';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css'],
})
export class TransactionComponent implements OnInit {
  transactions: any[] = [];
  userProfile: any;
  transactionType: string = 'sent';

  constructor(
    private cookieService: CookieService,
    private transactionService: TransactionService,
    private userService: UserService,
    private router: Router,
    private AuthService: AuthService
  ) {}

  ngOnInit(): void {
    this.getUserProfileData();
    this.getUserTransactions({
      target: { value: 'sentTransactions' },
    } as unknown as Event);
  }

  getUserProfileData(): any {
    this.userService.userProfile().subscribe(
      (res: any) => {
        if (res.code == 200) {
          this.userProfile = res.data;
        }
      },
      (error) => {
        if (error.status == 401) {
          this.cookieService.remove('token');
          this.router.navigate(['/auth/login']);
        }
      }
    );
  }

  getUserTransactions(event: Event): any {
    const action = (event.target as HTMLSelectElement).value;
    switch (action) {
      case 'sentTransactions':
        this.transactionType = 'sent';
        break;
      case 'receivedTransactions':
        this.transactionType = 'received';
        break;
      default:
        this.transactionType = 'sent';
        break;
    }

    this.transactionService.transaction(action).subscribe(
      (res: any) => {
        if (res.code == 200) {
          this.transactions = res.data;
        }
      },
      (error) => {
        if (error.status == 401) {
          this.cookieService.remove('token');
          this.router.navigate(['/auth/login']);
        }
      }
    );
  }

  logout(): void {
    this.AuthService.logout().subscribe((res: any) => {
        this.cookieService.remove('token');
        this.router.navigate(['/auth/login']);
    },
    error => {
      console.log(error);
    });
  }
}
