export class Bank {
  constructor(
    public iban: string,
    public swift: string,
    public country: string,
    public id? : number) {
  }
}
