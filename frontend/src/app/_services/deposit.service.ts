import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UploadRequest} from "../model/UploadRequest";
import {Observable} from "rxjs";

const API_URL = 'http://localhost:8080/api/upload';

@Injectable({
  providedIn: 'root'
})
export class DepositService {

  constructor(private httpClient: HttpClient) { }

  makeDeposit(request: UploadRequest): Observable<any> {
    return this.httpClient.post(API_URL, request);
  }
}
