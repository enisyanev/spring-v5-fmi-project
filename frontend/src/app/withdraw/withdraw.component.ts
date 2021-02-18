import {Component, OnInit} from '@angular/core';
import {PaymentInstrumentsService} from "../_services/payment-instruments.service";
import {Card} from "../model/Card";
import {Bank} from "../model/Bank";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AccountService} from "../_services/account.service";
import {Account} from "../model/Account";
import {TokenStorageService} from "../_services/token-storage.service";
import {UploadRequest} from "../model/UploadRequest";
import {WithdrawService} from "../_services/withdraw.service";
import {Router} from "@angular/router";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {ErrorSnackbarComponent} from "../shared/error-snackbar/error-snackbar.component";

@Component({
  selector: 'app-withdraw',
  templateUrl: './withdraw.component.html',
  styleUrls: ['./withdraw.component.css']
})
export class WithdrawComponent implements OnInit {

  displayedColumns: string[] = ['cardType', 'cardNumber'];
  bankDisplayedColumns: string[] = ['iban', 'swift', 'country'];

  instrumentChosen: boolean = false;
  showCards: boolean = false;
  showBanks: boolean = false;
  openAddCard: boolean = false;
  openAddBank: boolean = false;
  cards: Card[] = [];
  banks: Bank[] = [];
  accounts: Account[] = [];

  makeWithdraw: boolean = false;
  chosenId: number = -1;
  withdrawType: string = '';

  cardForm = new FormGroup({
    name: new FormControl('', Validators.required),
    cardNumber: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{16}$/)]),
    cardType: new FormControl('', Validators.required)
  });

  bankForm = new FormGroup({
    iban: new FormControl('', Validators.required),
    swift: new FormControl('', Validators.required),
    country: new FormControl('', Validators.required)
  });

  withdrawForm = new FormGroup({
    accountId: new FormControl('', Validators.required),
    amount: new FormControl({
      value: '',
      disabled: true
    }, [Validators.required, Validators.min(0.000001)]),
    currency: new FormControl({value: '', disabled: true}, Validators.required)
  });

  showSuccessMessage: boolean = false;

  constructor(private paymentInstrumentsService: PaymentInstrumentsService,
              private accountService: AccountService,
              private tokenStorage: TokenStorageService,
              private withdrawService: WithdrawService,
              private router: Router,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.instrumentChosen = false;
    this.showCards = false;
    this.showBanks = false;

    this.paymentInstrumentsService.getPaymentInstruments()
      .subscribe(instruments => {
        this.cards = instruments.cards;
        this.banks = instruments.banks;
      });

    this.accountService.getAllAccountByUsername(this.tokenStorage.getUser())
      .subscribe(data => this.accounts = data,
        error => console.log(error));
  }

  openCards() {
    this.instrumentChosen = true;
    this.showCards = true;
  }

  openBanks() {
    this.instrumentChosen = true;
    this.showBanks = true;
  }

  backToOptions() {
    this.showCards = false;
    this.showBanks = false;
    this.instrumentChosen = false;
  }

  openWithdraw(id: number, withdrawType: string) {
    this.showCards = false;
    this.showBanks = false;
    this.makeWithdraw = true;
    this.chosenId = id;
    this.withdrawType = withdrawType;
  }

  openAddCards() {
    this.showCards = false;
    this.openAddCard = true;
  }

  backToCards() {
    this.openAddCard = false;
    this.makeWithdraw = false;
    this.showCards = true;
    this.cardForm.reset();
    this.withdrawForm.reset();
    this.withdrawForm.get('amount')?.disable();
    this.withdrawForm.get('currency')?.disable();
  }

  addCard() {
    if (!this.cardForm.valid) {
      return;
    }

    const newCard: Card = {
      name: this.cardForm.controls.name.value,
      cardNumber: this.cardForm.controls.cardNumber.value,
      cardType: this.cardForm.controls.cardType.value
    };

    this.paymentInstrumentsService.addCard(newCard)
      .subscribe(card => {
        this.cards.push(card);
        this.openAddCard = false;
        this.showCards = true;
      }, error => console.log(error));

  }

  openAddBanks() {
    this.showBanks = false;
    this.openAddBank = true;
  }

  addBank() {
    if (!this.bankForm.valid) {
      return;
    }

    const newBank: Bank = {
      iban: this.bankForm.get('iban')?.value,
      swift: this.bankForm.get('swift')?.value,
      country: this.bankForm.get('country')?.value
    };

    this.paymentInstrumentsService.addBank(newBank)
      .subscribe(bank => {
        this.banks.push(bank);
        this.openAddBank = false;
        this.showBanks = true;
      }, error => console.log(error));
  }

  backToBanks() {
    this.openAddBank = false;
    this.makeWithdraw = false;
    this.showBanks = true;
    this.bankForm.reset();
    this.withdrawForm.reset();
    this.withdrawForm.get('amount')?.disable();
    this.withdrawForm.get('currency')?.disable();
  }

  doWithdraw() {
    if (!this.withdrawForm.valid) {
      return;
    }

    const uploadRequest: UploadRequest = {
      accountId: this.withdrawForm.get('accountId')?.value,
      instrumentId: this.chosenId,
      amount: this.withdrawForm.get('amount')?.value,
      currency: this.withdrawForm.get('currency')?.value,
      type: this.withdrawType
    };

    this.withdrawService.makeWithdraw(uploadRequest).subscribe(
      () => {
        this.showSuccessMessage = true;
        this.makeWithdraw = false;
      }, error => {
        this.showFailedSnackBar(error.error.message);
        console.log(error.error.message);
      });
  }

  showFailedSnackBar(message: string) {
    const snackBarRef = this.snackBar.openFromComponent(ErrorSnackbarComponent, {data: {message: message}});
    snackBarRef._dismissAfter(2000);
  }

  goToHomePage() {
    this.router.navigate(['/home']);
  }

  makeAnotherWithdraw() {
    this.instrumentChosen = false;
    this.showSuccessMessage = false;
    this.withdrawForm.reset();
    this.withdrawForm.get('amount')?.disable();
    this.withdrawForm.get('currency')?.disable();
  }

  accountChanged() {
    this.withdrawForm.get('amount')?.enable();
    this.withdrawForm.get('currency')?.enable();
  }
}
