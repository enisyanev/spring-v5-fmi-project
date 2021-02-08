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

  public getPagableTransactions(pageNumber: number, pageSize: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('pageNo', pageNumber.toString());
    params = params.append('pageSize', pageSize.toString());
    return this.http.get("http://localhost:8080/api/transactions-history", { params: params });
  }

}