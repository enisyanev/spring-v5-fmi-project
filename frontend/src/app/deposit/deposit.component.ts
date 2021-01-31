import { Component, OnInit } from '@angular/core';
import {PaymentInstrumentsService} from "../_services/payment-instruments.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Card} from "../model/Card";

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {

  instrumentChosen: boolean = false;
  cardChosen: boolean = false;

  cardForm = new FormGroup({
    name: new FormControl('', Validators.required),
    cardNumber: new FormControl('', Validators.required),
    cardType: new FormControl('', Validators.required)
  });

  constructor(private paymentInstrumentsService: PaymentInstrumentsService) { }

  ngOnInit(): void {
  }

  openCards() {
    this.instrumentChosen = true;
    this.cardChosen = true;
  }

  addCard() {
    const newCard: Card = {
      name: this.cardForm.controls.name.value,
      cardNumber: this.cardForm.controls.cardNumber.value,
      type: this.cardForm.controls.cardType.value
    };

    this.paymentInstrumentsService.addCard(newCard)
      .subscribe(card => console.log(card), error => console.log(error));

  }

}
