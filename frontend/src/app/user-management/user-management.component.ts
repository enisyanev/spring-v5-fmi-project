import { Component, OnInit } from '@angular/core';
import { AddUserDto } from '../model/AddUserDto';
import { UserManagementService } from '../_services/user-management.service';

@Component({
    selector: 'user-management',
    templateUrl: './user-management.component.html',
    styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
    form: AddUserDto = {
        password: '',
        firstName: '',
        lastName: '',
        username: ''
    };
    isSuccessful = false;
    isSignUpFailed = false;
    errorMessage = '';

    constructor(private userManagementService: UserManagementService) {
    }
    ngOnInit(): void {
    }

    onSubmit(): void {
        this.userManagementService.addUser(this.form).subscribe(
            data => {
                this.isSuccessful = true;
                this.isSignUpFailed = false;
            },
            err => {
                this.errorMessage = err.error.message;
                this.isSignUpFailed = true;
            }
        );
    }
}
