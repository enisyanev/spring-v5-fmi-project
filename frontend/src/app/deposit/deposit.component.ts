import {Component, OnInit} from '@angular/core';
import {PaymentInstrumentsService} from "../_services/payment-instruments.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Card} from "../model/Card";
import {UploadRequest} from "../model/UploadRequest";
import {TokenStorageService} from "../_services/token-storage.service";
import {AccountService} from "../_services/account.service";
import {Account} from "../model/Account";
import {DepositService} from "../_services/deposit.service";
import {Router} from "@angular/router";
import {Bank} from "../model/Bank";

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {

  displayedColumns: string[] = ['cardType', 'cardNumber'];
  bankDisplayedColumns: string[] = ['iban', 'swift', 'country'];

  instrumentChosen: boolean = false;
  showCards: boolean = false;
  showBanks: boolean = false;
  openAddCard: boolean = false;
  openAddBank: boolean = false;
  makeDeposit: boolean = false;
  chosenId: number = -1;
  depositType: string = '';
  cards: Card[] = [];
  banks: Bank[] = [];
  accounts: Account[] = [];

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

  depositForm = new FormGroup({
    accountId: new FormControl('', Validators.required),
    amount: new FormControl({ value: '', disabled: true }, [Validators.required, Validators.min(0.000001)]),
    currency: new FormControl({ value: '', disabled: true }, Validators.required)
  });

  showSuccessMessage: boolean = false;

  constructor(private paymentInstrumentsService: PaymentInstrumentsService,
              private tokenStorage: TokenStorageService,
              private accountService: AccountService,
              public depositService: DepositService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.instrumentChosen = false;
    this.showCards = false;
    this.showBanks = false;
    this.openAddCard = false;
    this.openAddBank = false;
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

  openAddCards() {
    this.showCards = false;
    this.openAddCard = true;
  }

  openAddBanks() {
    this.showBanks = false;
    this.openAddBank = true;
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

  openDeposit(id: number, depositType: string) {
    this.showCards = false;
    this.showBanks = false;
    this.makeDeposit = true;
    this.chosenId = id;
    this.depositType = depositType;
  }

  doDeposit() {
    if (!this.depositForm.valid) {
      return;
    }

    const uploadRequest: UploadRequest = {
      accountId: this.depositForm.get('accountId')?.value,
      instrumentId: this.chosenId,
      amount: this.depositForm.get('amount')?.value,
      currency: this.depositForm.get('currency')?.value,
      type: this.depositType
    };

    this.depositService.makeDeposit(uploadRequest).subscribe(
      () => {
        this.showSuccessMessage = true;
        this.makeDeposit = false;
    }, error => console.log(error));
  }

  goToHomePage() {
    this.router.navigate(['/home']);
  }

  makeAnotherDeposit() {
    this.instrumentChosen = false;
    this.showSuccessMessage = false;
    this.depositForm.reset();
    this.depositForm.get('amount')?.disable();
    this.depositForm.get('currency')?.disable();
  }

  backToOptions() {
    this.showCards = false;
    this.showBanks = false;
    this.instrumentChosen = false;
  }

  backToCards() {
    this.openAddCard = false;
    this.makeDeposit = false;
    this.showCards = true;
    this.cardForm.reset();
    this.depositForm.reset();
    this.depositForm.get('amount')?.disable();
    this.depositForm.get('currency')?.disable();
  }

  backToBanks() {
    this.openAddBank = false;
    this.makeDeposit = false;
    this.showBanks = true;
    this.bankForm.reset();
    this.depositForm.reset();
    this.depositForm.get('amount')?.disable();
    this.depositForm.get('currency')?.disable();
  }

  accountChanged() {
    this.depositForm.get('amount')?.enable();
    this.depositForm.get('currency')?.enable();
  }
}
