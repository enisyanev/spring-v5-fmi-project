import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AddUserDto } from '../model/AddUserDto';

const AUTH_API = 'http://localhost:8080/api/';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
    providedIn: 'root'
})
export class UserManagementService {
    constructor(private http: HttpClient) { }

    addUser(request: AddUserDto, permissions: string[]): Observable<any> {
        request.permissions = permissions;
        return this.http.post(AUTH_API + 'user-management', request, httpOptions);
    }

}