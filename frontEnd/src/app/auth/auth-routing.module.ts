import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { IsLoginGuard } from '../guards/is-login.guard';

const routes: Routes = [
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [IsLoginGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [IsLoginGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {}
