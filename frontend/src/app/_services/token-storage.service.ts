import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const PERMISSIONS_KEY = 'auth-permissions';
@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  constructor() { }

  signOut(): void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return {};
  }

  public savePermissions(permissions: string[]): void {
    window.sessionStorage.removeItem(PERMISSIONS_KEY);
    window.sessionStorage.setItem(PERMISSIONS_KEY, JSON.stringify(permissions));
  }

  public getPermissions() {
    let permissions = window.sessionStorage.getItem(PERMISSIONS_KEY);
    if (permissions) {
      return JSON.parse(permissions);
    }
    return [];
  }
}
