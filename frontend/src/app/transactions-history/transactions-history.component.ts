import { Component, OnInit } from '@angular/core';
import { TransactionsHistoryService } from '../_services/transactions-history.service';
import { saveAs } from 'file-saver';
import * as FileSaver from 'file-saver';

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
}
