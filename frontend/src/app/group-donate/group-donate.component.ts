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
    selector: 'donate-group',
    templateUrl: './group-donate.component.html',
    styleUrls: ['./group-donate.component.css']
  })
  export class GroupDonateComponent implements OnInit {
    form: any = {
      group: null,
      money: null,
      currency:null
    };
    groups = [
        {id: 0, groupName: ""},
    ];
    selectedGroup = "";
    currencies = [
        {id: 0, currency: ""},
    ];
    selectedCurrency = "";
    done=false;
    response="";
    constructor(private groupService: GroupService,private tokenS:TokenStorageService,private accountService:AccountService) { }
    ngOnInit(): void {
        this.groupService.getAllGroups().subscribe(res => {
            this.groups=res
        })
        this.accountService.getAllAccountByUsername(this.tokenS.getUser()).subscribe(res=>{
            this.currencies=res
        })

    }
    onSubmit(): void {
        const {  money,currency } = this.form;
        if(this.currencies.length==1){
           this.selectedCurrency=this.currencies[0].currency
        }
        if(this.groups.length==1){
            this.selectedGroup=this.groups[0].groupName
        }
        this.groupService.donateGroup(this.selectedGroup,money,this.selectedCurrency).subscribe(res=>{
            this.response=res.message
        })
        this.done=true;
    }

}
