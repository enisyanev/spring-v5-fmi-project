import { Component, OnInit, ChangeDetectorRef} from '@angular/core';
import { TokenStorageService } from '../_services/token-storage.service';
import { GroupService } from '../_services/group.service';
import { AccountService } from '../_services/account.service';
import { RecurringService } from '../_services/recurring.service';
import { DatePipe } from '@angular/common';
  
@Component({
    selector: 'reccuring',
    templateUrl: './reccuring.component.html',
    styleUrls: ['./reccuring.component.css']
  }) 
  export class ReccuringComponent implements OnInit {
    form: any = {
      email: null,
      amount: null,
      period:null
    };
    pipe = new DatePipe('en-US');
    reccurings = [
        {receiver: "test",amount:1.0,lastDate:this.pipe.transform(new Date(), 'short'),nextDate:new Date(),period:"MONTHLY"},
    ];
    displayedColumns: string[] = ['receiver', 'amount', 'lastExecutionTime', 'nextExecutionTime','period'];
    createRecurring=false;
    periods=[
      {name:"DAILY"},
      {name:"WEEKLY"},
      {name:"MONTHLY"},
    ]
    selectedPeriod=""
    currencies = [
      {id: 0, currency: ""},
    ];
    selectedCurrency = "";
    constructor(private groupService: GroupService,private changeDetectorRefs: ChangeDetectorRef,private recurringService:RecurringService,private tokenS:TokenStorageService,private accountService:AccountService) { }
    ngOnInit(): void {
       this.recurringService.getAllRecurrings().subscribe(res => {
          console.log(res)
            this.reccurings=res
            //this.changeDetectorRefs.reattach()
        })
        this.accountService.getAllAccountByUsername(this.tokenS.getUser()).subscribe(res=>{
          this.currencies=res
      })
    }
    openReccuring() {
      this.createRecurring = true;
    }
    closeReccuring() {
      this.createRecurring = false;
    }
    onSubmit(): void {
      const {  email,amount } = this.form;
      let accountId=0
      for(let i=0; i<this.currencies.length; i++){
        if(this.currencies[i].currency==this.selectedCurrency)accountId=this.currencies[i].id
      }
      this.recurringService.createRecurring(accountId,amount,email,this.selectedPeriod)
    }
      
}