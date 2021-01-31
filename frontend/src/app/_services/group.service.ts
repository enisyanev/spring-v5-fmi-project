import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
@Injectable({
    providedIn: 'root'
})
export class GroupService {
    constructor(private http: HttpClient) { }


  public createGroup(groupName: string,money :bigint,description:string): void {

    this.http.post('http://localhost:8080/api/group/create', {
            "groupName":groupName,
            "targetMoney":money,
            "groupDescription":description
          }, httpOptions).subscribe(res => {
            console.log(res)
        });
  }
  public donateGroup(groupName:string,money:bigint,currency:string):Observable<any>{
    return this.http.post('http://localhost:8080/api/group/donate', {
        "groupName":groupName,
        "money":money,
        "currency":currency
      }, httpOptions)
  }
  public getAllGroups():Observable<any>{
    return this.http.get('http://localhost:8080/api/group', httpOptions)
  }
}
