import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data.expectedRole;
    if (!this.authService.isLoggedIn) {
      this.router.navigate(['login']);
      return false;
    }

    if (expectedRole && this.authService.permissions && !this.authService.permissions.includes(expectedRole)) {
      this.router.navigate(['/home']);
      return false;
    }

    return true;
  }
}
