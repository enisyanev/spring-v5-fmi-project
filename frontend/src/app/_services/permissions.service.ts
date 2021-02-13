import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class PermissionsService {

    constructor(private http: HttpClient) { }

    getPermissions(): Observable<any> {
        return this.http.get("http://localhost:8080/api/permissions");
    }
}
