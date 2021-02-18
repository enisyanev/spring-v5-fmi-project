import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegistrationDto } from "../model/RegistrationDto";

const AUTH_API = 'http://localhost:8080/api/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public isLoggedIn: boolean = false;

  public CAN_DEPOSIT: boolean = false;
  public CAN_WITHDRAW: boolean = false;
  public CAN_DONATE: boolean = false;
  public CAN_CREATE_GROUP: boolean = false;
  public CAN_SEND_MONEY: boolean = false;
  public CAN_CREATE_ACCOUNT: boolean = false;
  public CAN_USE_RECCURINGS: boolean = false;
  public CAN_SEE_TR_HISTORY: boolean = false;
  public CAN_USE_USER_MANAGEMENT: boolean = false;

  public permissions: any;

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'login', {
      username,
      password
    }, httpOptions);
  }

  register(request: RegistrationDto): Observable<any> {
    return this.http.post(AUTH_API + 'register', request, httpOptions);
  }

  fillPermissions(permissions: any) {
    this.CAN_DEPOSIT = permissions.indexOf("CAN_DEPOSIT") > -1;
    this.CAN_WITHDRAW = permissions.indexOf("CAN_WITHDRAW") > -1;
    this.CAN_DONATE = permissions.indexOf("CAN_DONATE") > -1;
    this.CAN_CREATE_GROUP = permissions.indexOf("CAN_CREATE_GROUP") > -1;
    this.CAN_SEND_MONEY = permissions.indexOf("CAN_SEND_MONEY") > -1;
    this.CAN_CREATE_ACCOUNT = permissions.indexOf("CAN_CREATE_ACCOUNT") > -1;
    this.CAN_USE_RECCURINGS = permissions.indexOf("CAN_USE_RECCURINGS") > -1;
    this.CAN_SEE_TR_HISTORY = permissions.indexOf("CAN_SEE_TR_HISTORY") > -1;
    this.CAN_USE_USER_MANAGEMENT = permissions.indexOf("CAN_USE_USER_MANAGEMENT") > -1;
    this.permissions = permissions;
  }
}
