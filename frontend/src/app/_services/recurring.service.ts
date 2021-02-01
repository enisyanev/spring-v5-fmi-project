import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
@Injectable({
    providedIn: 'root'
})
export class RecurringService {
  constructor(private http: HttpClient) { }

  public getAllRecurrings():Observable<any>{
    return this.http.get('http://localhost:8080/api/recurring-payments', httpOptions)
  }
  public createRecurring(accountId:number,amount:number,email:string,period:string):void{
    this.http.post('http://localhost:8080/api/recurring-payments', {
        "accountId":accountId,
        "amount":amount,
        "email":email,
        "period":period
      }, httpOptions).subscribe(res => {
        console.log(res)
    });  
  }
}
