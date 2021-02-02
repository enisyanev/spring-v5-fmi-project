import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json',"Accept": "application/json" })
  };
@Injectable({
    providedIn: 'root'
})
export class AccountService {
    constructor(private http: HttpClient) { }

  public getAllAccountByUsername(username:string):Observable<any>{
    return this.http.get('http://localhost:8080/api/account?username='+username, httpOptions)
  }
  public createAccount(username:string,currency:string):void{
    this.http.post('http://localhost:8080/api/account', {
            "username":username,
            "currency":currency,
          }, httpOptions).subscribe(res => {
            console.log(res)
        });
  }
}
