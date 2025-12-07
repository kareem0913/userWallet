import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransactionComponent } from './transaction/components/transaction/transaction.component';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: 'transactions',
    loadChildren: () =>
      import('./transaction/transaction.module').then(
        (m) => m.TransactionModule
      ),
  },
  {
    path: '',
    redirectTo: 'transactions',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'transactions',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
