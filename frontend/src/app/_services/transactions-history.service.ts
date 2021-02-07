import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionsHistoryService {
  constructor(private http: HttpClient) { }

  public downloadHistory(): Observable<Blob> {
    return this.http.get("http://localhost:8080/api/transactions-history/csv", { responseType: 'blob' });
  }

}