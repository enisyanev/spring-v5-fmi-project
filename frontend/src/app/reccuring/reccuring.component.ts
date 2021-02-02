import { Component, OnInit, ChangeDetectorRef} from '@angular/core';
import { TokenStorageService } from '../_services/token-storage.service';
import { GroupService } from '../_services/group.service';
import { AccountService } from '../_services/account.service';
import { RecurringService } from '../_services/recurring.service';
import { DatePipe } from '@angular/common';
import { Inject } from '@angular/core';
import {MatDialog, MatDialogConfig, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

export interface DialogData {
  period: string;
  amount:number;
}
@Component({
    selector: 'reccuring',
    templateUrl: './reccuring.component.html',
    styleUrls: ['./reccuring.component.css']
  }) 
  export class ReccuringComponent implements OnInit {
    form: any = {
      amount: null,
      period:null
    };
    amount=null;
    period="";
    pipe = new DatePipe('en-US');
    reccurings = [
        {id:0,receiver: "test",amount:1.0,lastDate:this.pipe.transform(new Date(), 'short'),nextDate:new Date(),period:"MONTHLY"},
    ];
    displayedColumns: string[] = ['receiver', 'amount', 'lastExecutionTime', 'nextExecutionTime','period','action'];
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
    constructor(public dialog: MatDialog,private groupService: GroupService,private changeDetectorRefs: ChangeDetectorRef,private recurringService:RecurringService,private tokenS:TokenStorageService,private accountService:AccountService) { }
    ngOnInit(): void {
        this.getAllRecurrings()
        this.accountService.getAllAccountByUsername(this.tokenS.getUser()).subscribe(res=>{
          this.currencies=res
      })
    }
    deleteRecurring(id:number) {
      this.recurringService.deleteRecurring(id)
      location.reload()
    }
    openDialog(id:number) {
      console.log(id)
    }
    getAllRecurrings(){
      this.recurringService.getAllRecurrings().subscribe(res => {
        console.log(res)
          this.reccurings=res
          this.changeDetectorRefs.reattach()
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
      location.reload()
    }
    getPeriodForThisRecurring(id:number):string{
      let period=""
      for(let i=0; i<this.reccurings.length; i++){
        if(this.reccurings[i].id==id)period=this.reccurings[i].period
      }
       return period
    }
    openDialogBox(id:number): void {
      console.log(id)
      const dialogRef = this.dialog.open(DialogEditReccuring, {
        width: '250px',
        data: {amount:this.amount,period:this.period}
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        if(result){
          let period=result.period
          if(result.period==""){
            period=this.getPeriodForThisRecurring(id)
          }
          if(result.period!="WEEKLY"&&result.period!="DAILY"&&result.period!="MONTHLY"){
            window.alert("Period should be WEEKLY,MONTHLY or DAILY")
          }else{
            this.recurringService.updateRecurring(id,result.amount,period)
            location.reload()
          }
        }
      });
    }
      
}
@Component({
  selector: 'dialog-reccuing',
  templateUrl: 'reccuring.dialog.component.html',
})
export class DialogEditReccuring {

  constructor(
    public dialogRef: MatDialogRef<DialogEditReccuring>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
      dialogRef.disableClose = true;
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

}