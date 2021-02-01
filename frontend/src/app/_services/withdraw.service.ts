import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UploadRequest} from "../model/UploadRequest";
import {Observable} from "rxjs";

const API_URL = 'http://localhost:8080/api/withdraw';

@Injectable({
  providedIn: 'root'
})
export class WithdrawService {

  constructor(private httpClient: HttpClient) { }

  makeWithdraw(request: UploadRequest): Observable<any> {
    return this.httpClient.post(API_URL, request);
  }
}
