import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';
import { CookieService } from '../../../services/cookie.service';

@Component({
  selector: 'app-transfer-moeny',
  templateUrl: './transfer-moeny.component.html',
  styleUrls: ['./transfer-moeny.component.css'],
})
export class TransferMoenyComponent implements OnInit {

  transferForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private cookieService: CookieService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.transferForm = this.fb.group({
      receiverPhone: ['', [Validators.required]],
      amount: ['', [Validators.required, Validators.min(1)]]
    });
  }

  submitTransfer(): void {
    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    this.transactionService.transferMoney(this.transferForm.value).subscribe(
      (res: any) => {
        if (res.code === 200) {
          this.router.navigate(['/transactions']);
        } else {
          alert(res.errors ?? res.message);
        }
      },
      (error: any) => {
        if (error.status === 401) {
          this.cookieService.remove('token');
          this.router.navigate(['/auth/login']);
        }
        console.error('Transfer failed', error);
      }
    );
  }

}
