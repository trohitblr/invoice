import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalouge } from '../catalouge.model';
import { CatalougeService } from '../service/catalouge.service';

@Injectable({ providedIn: 'root' })
export class CatalougeRoutingResolveService implements Resolve<ICatalouge | null> {
  constructor(protected service: CatalougeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalouge | null | never> {
    const id = route.params['catalougeId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalouge: HttpResponse<ICatalouge>) => {
          if (catalouge.body) {
            return of(catalouge.body);
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
