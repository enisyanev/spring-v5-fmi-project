import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { TokenStorageService } from '../_services/token-storage.service';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroupService } from '../_services/group.service';
const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json',"Accept": "application/json" })
  };
@Component({
    selector: 'create-group',
    templateUrl: './group.component.html',
    styleUrls: ['./group.component.css']
  })
  export class GroupComponent implements OnInit {
    form: any = {
      group: null,
      money: null
    };
    isLoggedIn = false;
    done=false;
    constructor(private groupService: GroupService,private http: HttpClient) { }
    ngOnInit(): void {
    }
    onSubmit(): void {
        const { group, money,description } = this.form;
        this.groupService.createGroup(group,money,description)
        this.done=true;
    }
      
}