import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../_services/token-storage.service';
import {AccountService} from '../_services/account.service';
import {SendMoneyService} from '../_services/send-money.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Account} from "../model/Account";
import {finalize} from "rxjs/operators";

@Component({
  selector: 'send-money',
  templateUrl: './send.money.component.html',
  styleUrls: ['./send.money.component.css']
})
export class SendMoneyComponent implements OnInit {
  formGroup: FormGroup = new FormGroup({
    email: new FormControl('', Validators.required),
    money: new FormControl('', [Validators.required, Validators.min(0.000001)]),
    currency: new FormControl('', Validators.required)
  });

  accounts: Account[] = [];

  done = false;
  success = false;
  failed = false;
  scheduled = false;
  response = "";

  constructor(private tokenS: TokenStorageService,
              private accountService: AccountService,
              private sendMoneyService: SendMoneyService) {
  }

  ngOnInit(): void {
    this.accountService.getAllAccountByUsername(this.tokenS.getUser()).subscribe(res => {
      this.accounts = res;
    })

  }

  onSubmit(): void {
    const email = this.formGroup.get('email')?.value;
    const money = this.formGroup.get('money')?.value;
    const accountId = this.formGroup.get('currency')?.value;
    const currency = this.accounts.find(elem => elem.id == accountId)?.currency;
    this.sendMoneyService.sendMoney(email, this.tokenS.getUser(), money,
      currency == null ? 'EUR' : currency, accountId)
      .pipe(
        finalize(() => this.done = true)
      ).subscribe(res => {
        if (res.status == "PROCESSED") {
          this.response = "Money send successfully";
          this.success = true;
        } else if (res.status == "SCHEDULED") {
          this.response = "The recipient is not registered! Your transaction is scheduled.";
          this.scheduled = true;
        } else {
          this.response = "Something went wrong";
          this.failed = true;
        }
      }, error =>  {
        this.response = error.error.message;
        this.failed = true;
      });
  }

}
