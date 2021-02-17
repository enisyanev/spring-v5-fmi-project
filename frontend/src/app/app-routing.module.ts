import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { GroupComponent } from './group/group.component';
import { GroupDonateComponent } from './group-donate/group-donate.component';
import { DepositComponent } from "./deposit/deposit.component";
import { SendMoneyComponent } from './sendMoney/send.money.component';
import { AccountComponent } from './account/account.component';
import { ReccuringComponent } from './reccuring/reccuring.component';
import { WithdrawComponent } from "./withdraw/withdraw.component";
import { TransactionsHistoryComponent } from './transactions-history/transactions-history.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { ExchangeComponent } from './exchange/exchange.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'group', component: GroupComponent },
  { path: 'group/donate', component: GroupDonateComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'deposit', component: DepositComponent },
  { path: 'withdraw', component: WithdrawComponent },
  { path: 'send-money', component: SendMoneyComponent },
  { path: 'account/create', component: AccountComponent },
  { path: 'reccuring', component: ReccuringComponent },
  { path: 'transactions/history', component: TransactionsHistoryComponent },
  { path: 'users/management', component: UserManagementComponent },
  { path: 'exchange', component: ExchangeComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
