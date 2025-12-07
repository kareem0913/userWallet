import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { TransactionComponent } from './components/transaction/transaction.component';
import { AuthGuard } from '../guards/auth.guard';
import { TransferMoenyComponent } from './components/transfer-moeny/transfer-moeny.component';

const routes: Routes = [
  {
    path : "",
     component: TransactionComponent,
     canLoad: [AuthGuard],
     canActivate: [AuthGuard],
    },
    {
      path : "transfer-money",
      component: TransferMoenyComponent,
      canLoad: [AuthGuard],
      canActivate: [AuthGuard],
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransactionRoutingModule { }
