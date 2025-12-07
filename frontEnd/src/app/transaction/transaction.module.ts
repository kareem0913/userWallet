import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { TransactionRoutingModule } from './transaction-routing.module';
import { TransactionComponent } from './components/transaction/transaction.component';
import { TransferMoenyComponent } from './components/transfer-moeny/transfer-moeny.component';


@NgModule({
  declarations: [
    TransactionComponent,
    TransferMoenyComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    TransactionRoutingModule
  ]
})
export class TransactionModule { }
