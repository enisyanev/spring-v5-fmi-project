import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class SendMoneyService {
  constructor(private http: HttpClient) { }


  public sendMoney(receiverEmail: string, username: string, money: bigint, currency: string, accountId: number): Observable<any> {
    return this.http.post('http://localhost:8080/api/send-money', {
      "email": receiverEmail,
      "username": username,
      "amount": money,
      "currency": currency,
      "accountId": accountId
    }, httpOptions)
  }
  
  uploadFile(file: File): Observable<any> {
    let url = "http://localhost:8080/api/send-money/csv";
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return this.http.post(url, formdata);
  }

}
