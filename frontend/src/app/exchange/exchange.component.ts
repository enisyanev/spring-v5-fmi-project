import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { AccountService } from '../_services/account.service';
import { SendMoneyService } from '../_services/send-money.service';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'exchange',
  templateUrl: './exchange.component.html',
  styleUrls: ['./exchange.component.css']
})
export class ExchangeComponent implements OnInit {
  formGroup: FormGroup = new FormGroup({
    money: new FormControl('', [Validators.required, Validators.min(0.000001)]),
    currency: new FormControl('', Validators.required),
    newCurrency: new FormControl('', Validators.required)
  });

  accounts: Account[] = [];
  accountsTo: Account[] = [];
  selectedOption!: string;
  selectedOption2!: string;

  done = false;
  success = false;
  failed = false;
  scheduled = false;
  response = "";

  selectedFile!: File;
  @ViewChild('file') file!: ElementRef;


  constructor(private tokenS: TokenStorageService,
    private accountService: AccountService,
    private sendMoneyService: SendMoneyService) {
  }

  ngOnInit(): void {
    this.accountService.getAllAccountByUsername(this.tokenS.getUser()).subscribe(res => {
      this.accounts = res;
      this.accountsTo = res;
    })

  }

  onSubmit(): void {
    const money = this.formGroup.get('money')?.value;
    const accountId = this.formGroup.get('currency')?.value;
    const newAccountId = this.formGroup.get('newCurrency')?.value;

    this.sendMoneyService.exchangeMoney(money, accountId, newAccountId)
      .pipe(
        finalize(() => this.done = true)
      ).subscribe(res => {
        this.response = "Money excnahged successfully";
        this.success = true;
      }, error => {
        this.response = error.error.message;
        this.failed = true;
      });
  }

  onChange(selected: any) {
    this.accountsTo = this.accounts.filter(acc => acc.id != this.selectedOption);
  }
}