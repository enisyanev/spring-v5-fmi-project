import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { GroupComponent } from './group/group.component';
import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { GroupDonateComponent } from './group-donate/group-donate.component';
import { DepositComponent } from './deposit/deposit.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { SendMoneyComponent } from './sendMoney/send.money.component';
import { AccountComponent } from './account/account.component';
import { MatTableModule } from "@angular/material/table";
import { MatSortModule } from '@angular/material/sort';
import { CommonModule } from "@angular/common";
import { DialogEditReccuring, ReccuringComponent } from './reccuring/reccuring.component';
import { MatCardModule } from "@angular/material/card";
import { WithdrawComponent } from './withdraw/withdraw.component';
import { MatDialogModule } from '@angular/material/dialog';

import { MatSnackBarModule, MatSnackBarRef } from "@angular/material/snack-bar";
import { ErrorSnackbarComponent } from './shared/error-snackbar/error-snackbar.component';
import { MatIconModule } from "@angular/material/icon";
import { TransactionsHistoryComponent } from './transactions-history/transactions-history.component';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    GroupComponent,
    GroupDonateComponent,
    DepositComponent,
    SendMoneyComponent,
    AccountComponent,
    ReccuringComponent,
    WithdrawComponent,
    DialogEditReccuring,
    ErrorSnackbarComponent,
    TransactionsHistoryComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    MatSortModule,
    MatCardModule,
    MatDialogModule,
    MatSnackBarModule,
    MatIconModule,
    MatPaginatorModule
  ],
  entryComponents: [ReccuringComponent, DialogEditReccuring],
  providers: [authInterceptorProviders, {
    provide: MatSnackBarRef,
    useValue: {}
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
