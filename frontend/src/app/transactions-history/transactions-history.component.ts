import { Component, OnInit, ViewChild } from '@angular/core';
import { TransactionsHistoryService } from '../_services/transactions-history.service';
import * as FileSaver from 'file-saver';
import { Transaction } from '../model/Transaction';
import { TransactionsHistory } from '../model/TransactionsHistory';
import { PageEvent } from '@angular/material/paginator';

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
  currentSelectedPage = 0;
  pageSize = 10;
  length = this.totalPages * this.pageSize;
  pageEvent: PageEvent = new PageEvent;
  pageSizeOptions: number[] = [5, 10, 25, 100];
  pageNumber = 1;
  displayedColumns: string[] = ['id', 'amount', 'currency', 'createdTime', 'description'];

  constructor(private transactionsHistoryService: TransactionsHistoryService) {
  }

  ngOnInit(): void {
    this.getPage(1, this.pageSize);
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
          this.currentSelectedPage = transactionsHistory.pageNumber;
          this.length = this.totalPages * this.pageSize;
        },
        (error) => {
          console.log(error);
        }
      );
  }

  onPaginateChange(event: PageEvent) {
    let pageNumber = event.pageIndex + 1;
    let pageSize = event.pageSize;
    console.log(pageNumber);

    if (this.pageSize != pageSize) {
      console.log("reset");
      pageNumber = 1;
      this.pageSize = pageSize;
    }
    this.getPage(pageNumber, pageSize);
  }
}
