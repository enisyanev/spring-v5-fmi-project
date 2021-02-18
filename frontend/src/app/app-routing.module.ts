import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {RegisterComponent} from './register/register.component';
import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {GroupComponent} from './group/group.component';
import {GroupDonateComponent} from './group-donate/group-donate.component';
import {DepositComponent} from "./deposit/deposit.component";
import {SendMoneyComponent} from './sendMoney/send.money.component';
import {AccountComponent} from './account/account.component';
import {ReccuringComponent} from './reccuring/reccuring.component';
import {WithdrawComponent} from "./withdraw/withdraw.component";
import {TransactionsHistoryComponent} from './transactions-history/transactions-history.component';
import {UserManagementComponent} from './user-management/user-management.component';
import {ExchangeComponent} from './exchange/exchange.component';
import {AuthGuardService} from "./_services/auth-guard.service";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {
    path: 'group',
    component: GroupComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_CREATE_GROUP'}
  },
  {
    path: 'group/donate',
    component: GroupDonateComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_DONATE'}
  },
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {
    path: 'deposit',
    component: DepositComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_DEPOSIT'}
  },
  {
    path: 'withdraw',
    component: WithdrawComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_WITHDRAW'}
  },
  {
    path: 'send-money',
    component: SendMoneyComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_SEND_MONEY'}
  },
  {
    path: 'account/create',
    component: AccountComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_CREATE_ACCOUNT'}
  },
  {
    path: 'reccuring',
    component: ReccuringComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_USE_RECCURINGS'}
  },
  {
    path: 'transactions/history',
    component: TransactionsHistoryComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_SEE_TR_HISTORY'}
  },
  {
    path: 'users/management',
    component: UserManagementComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_USE_USER_MANAGEMENT'}
  },
  {
    path: 'exchange',
    component: ExchangeComponent,
    canActivate: [AuthGuardService],
    data: {expectedRole: 'CAN_SEND_MONEY'}
  },
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
