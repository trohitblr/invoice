import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRetailer } from '../retailer.model';
import { RetailerService } from '../service/retailer.service';

@Injectable({ providedIn: 'root' })
export class RetailerRoutingResolveService implements Resolve<IRetailer | null> {
  constructor(protected service: RetailerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRetailer | null | never> {
    const id = route.params['retailerId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((retailer: HttpResponse<IRetailer>) => {
          if (retailer.body) {
            return of(retailer.body);
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
