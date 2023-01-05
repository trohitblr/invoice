import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRetailInventory } from '../retail-inventory.model';
import { RetailInventoryService } from '../service/retail-inventory.service';

@Injectable({ providedIn: 'root' })
export class RetailInventoryRoutingResolveService implements Resolve<IRetailInventory | null> {
  constructor(protected service: RetailInventoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRetailInventory | null | never> {
    const id = route.params['retailInventoryId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((retailInventory: HttpResponse<IRetailInventory>) => {
          if (retailInventory.body) {
            return of(retailInventory.body);
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
