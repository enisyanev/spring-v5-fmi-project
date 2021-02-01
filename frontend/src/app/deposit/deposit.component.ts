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

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {

  displayedColumns: string[] = ['cardType', 'cardNumber'];

  instrumentChosen: boolean = false;
  showCards: boolean = false;
  openAddCard: boolean = false;
  makeDeposit: boolean = false;
  chosenId: number = -1;
  depositType: string = '';
  cards: Card[] = [];
  accounts: Account[] = [];

  cardForm = new FormGroup({
    name: new FormControl('', Validators.required),
    cardNumber: new FormControl('', Validators.required),
    cardType: new FormControl('', Validators.required)
  });

  depositForm = new FormGroup({
    accountId: new FormControl('', Validators.required),
    amount: new FormControl('', Validators.required),
    currency: new FormControl('', Validators.required)
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
    this.openAddCard = false;
    this.paymentInstrumentsService.getPaymentInstruments()
      .subscribe(instruments => {
        this.cards = instruments.cards
      });
    this.accountService.getAllAccountByUsername(this.tokenStorage.getUser())
      .subscribe(data => this.accounts = data,
        error => console.log(error));
  }

  openCards() {
    this.instrumentChosen = true;
    this.showCards = true;
  }

  openAddCards() {
    this.showCards = false;
    this.openAddCard = true;
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

  openDeposit(id: number, depositType: string) {
    this.showCards = false;
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
  }

  backToOptions() {
    this.showCards = false;
    this.instrumentChosen = false;
  }

  backToCards() {
    this.openAddCard = false;
    this.makeDeposit = false;
    this.showCards = true;
  }
}
