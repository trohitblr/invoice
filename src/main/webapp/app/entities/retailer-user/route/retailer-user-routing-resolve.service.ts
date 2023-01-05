import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRetailerUser } from '../retailer-user.model';
import { RetailerUserService } from '../service/retailer-user.service';

@Injectable({ providedIn: 'root' })
export class RetailerUserRoutingResolveService implements Resolve<IRetailerUser | null> {
  constructor(protected service: RetailerUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRetailerUser | null | never> {
    const id = route.params['retailerUserId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((retailerUser: HttpResponse<IRetailerUser>) => {
          if (retailerUser.body) {
            return of(retailerUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
