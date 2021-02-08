import { Component, OnInit } from '@angular/core';
import { TransactionsHistoryService } from '../_services/transactions-history.service';
import { saveAs } from 'file-saver';
import * as FileSaver from 'file-saver';
import { Transaction } from '../model/Transaction';
import { TransactionsHistory } from '../model/TransactionsHistory';

const MIME_TYPES = {
  pdf: 'application/pdf',
  xls: 'application/vnd.ms-excel',
  xlsx: 'application/vnd.openxmlformats-officedocument.spreadsheetxml.sheet'
}

@Component({
  selector: 'transactions-history',
  templateUrl: './transactions-history.component.html',
  styleUrls: ['./transactions-history.component.css']
})
export class TransactionsHistoryComponent implements OnInit {

  transactions: Transaction[] = [];
  totalPages = 0;
  pageIndexes: number[] = [];
  currentSelectedPage = 0;

  constructor(private transactionsHistoryService: TransactionsHistoryService) {
  }

  ngOnInit(): void {
  }

  downloadHistory() {
    this.transactionsHistoryService.downloadHistory().subscribe(data => {
      const blob1 = new Blob([data], { type: 'text/csv' });
      FileSaver.saveAs(blob1, 'transactionsHistory.csv');
    });
  }

  getPage(pageNumber: number, pageSize: number) {

    this.transactionsHistoryService.getPagableTransactions(pageNumber, pageSize)
      .subscribe(
        (transactionsHistory: TransactionsHistory) => {
          console.log(transactionsHistory);
          this.transactions = transactionsHistory.transactions;
          this.totalPages = transactionsHistory.totalPages;
          this.pageIndexes = Array(this.totalPages).fill(0).map((x, i) => i);
          this.currentSelectedPage = transactionsHistory.pageNumber;
        },
        (error) => {
          console.log(error);
        }
      );
  }
}
