import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Card} from "../model/Card";
import {Observable} from "rxjs";

const PAYMENT_INSTRUMENTS_API = 'http://localhost:8080/api/payment-instruments';

@Injectable({
  providedIn: 'root'
})
export class PaymentInstrumentsService {

  constructor(private httpClient: HttpClient) { }

  getPaymentInstruments() {

  }

  addCard(card: Card): Observable<Card> {
    return this.httpClient.post<Card>(
      PAYMENT_INSTRUMENTS_API + '/cards', card);
  }
}
