import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { TokenStorageService } from '../_services/token-storage.service';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroupService } from '../_services/group.service';
import { AccountService } from '../_services/account.service';
import { connectableObservableDescriptor } from 'rxjs/internal/observable/ConnectableObservable';

@Component({
    selector: 'account',
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.css']
  })
  export class AccountComponent implements OnInit {
    form: any = {
      currency:null
    };
    done=false;
    response="";
    constructor(private tokenS:TokenStorageService,private accountService:AccountService) { }
    ngOnInit(): void {
        
    }
    onSubmit(): void {
        const { currency } = this.form;
        this.accountService.createAccount(this.tokenS.getUser(),currency)
        this.response="Account Created"
        this.done=true;
    }
      
}