import { Transaction } from "./Transaction";

export class TransactionsHistory {
  constructor(
    public transactions: Transaction[],
    public totalPages: number,
    public pageNumber: number,
    public pageSize: number
  ) {
  }
}