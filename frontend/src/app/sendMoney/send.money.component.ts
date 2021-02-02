import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { TokenStorageService } from '../_services/token-storage.service';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroupService } from '../_services/group.service';
import { AccountService } from '../_services/account.service';
import { SendMoneyService } from '../_services/send-money.service';

@Component({
    selector: 'send-money',
    templateUrl: './send.money.component.html',
    styleUrls: ['./send.money.component.css']
  })
  export class SendMoneyComponent implements OnInit {
    form: any = {
      email: null,
      money: null,
      currency:null
    };
    currencies = [
        {id: 0, currency: ""},
    ];
    selectedCurrency = "";
    done=false;
    response="";
    constructor(private tokenS:TokenStorageService,private accountService:AccountService,private sendMoneyService:SendMoneyService) { }
    ngOnInit(): void {
        this.accountService.getAllAccountByUsername(this.tokenS.getUser()).subscribe(res=>{
            this.currencies=res
        })
        
    }
    onSubmit(): void {
        const {  email,money } = this.form;
        if(this.currencies.length==1){
            this.selectedCurrency=this.currencies[0].currency
        }
        let accountId=0
        for(let i=0; i<this.currencies.length; i++){
            if(this.currencies[i].currency==this.selectedCurrency)accountId=this.currencies[i].id; //use i instead of 0
        }
        this.sendMoneyService.sendMoney(email,this.tokenS.getUser(),money,this.selectedCurrency,accountId).subscribe(res=>{
            if(res.status=="PROCESSED")this.response="Money send successfully"
            else this.response="Soemthing went wrong"
        })
        this.done=true;
    }
      
}