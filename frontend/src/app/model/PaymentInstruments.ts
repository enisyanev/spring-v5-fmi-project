import {Card} from "./Card";
import {Bank} from "./Bank";

export class PaymentInstruments {
  constructor(
    public cards: Card[],
    public banks: Bank[]) {
  }
}
