import { Component, OnInit } from '@angular/core';
import { AddUserDto } from '../model/AddUserDto';
import { UserDto } from '../model/userDto';
import { PermissionsService } from '../_services/permissions.service';
import { UserManagementService } from '../_services/user-management.service';

@Component({
    selector: 'user-management',
    templateUrl: './user-management.component.html',
    styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
    isSuccessful = false;
    isSignUpFailed = false;
    errorMessage = '';
    permissions = [];
    selected = [];
    displayedColumns: string[] = ['id', 'username', "deleteButton"];
    users!: UserDto;
    form: AddUserDto = {
        password: '',
        firstName: '',
        lastName: '',
        username: '',
        permissions: this.selected
    };

    constructor(private userManagementService: UserManagementService, private permissionsService: PermissionsService) {
    }
    ngOnInit(): void {
        this.permissionsService.getPermissions().subscribe(
            data => this.permissions = data
        );
        this.loadUsers();
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
    loadUsers(): void {
        this.userManagementService.getUsers().subscribe(
            data => this.users = data
        );
    }

    deleteUser(username: string) {
        this.userManagementService.deleteUser(username).subscribe(
            data => this.users = data
        );
    }
}
