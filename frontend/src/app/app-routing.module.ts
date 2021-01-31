import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { GroupComponent } from './group/group.component';
import { GroupDonateComponent } from './group-donate/group-donate.component';
import { DepositComponent } from "./deposit/deposit.component";
import { SendMoneyComponent } from './sendMoney/send.money.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'group', component: GroupComponent },
  { path: 'group/donate', component: GroupDonateComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'deposit', component: DepositComponent },
  { path: 'send-money', component: SendMoneyComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
