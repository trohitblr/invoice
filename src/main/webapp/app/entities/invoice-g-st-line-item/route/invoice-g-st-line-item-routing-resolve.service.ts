import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceGStLineItem } from '../invoice-g-st-line-item.model';
import { InvoiceGStLineItemService } from '../service/invoice-g-st-line-item.service';

@Injectable({ providedIn: 'root' })
export class InvoiceGStLineItemRoutingResolveService implements Resolve<IInvoiceGStLineItem | null> {
  constructor(protected service: InvoiceGStLineItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceGStLineItem | null | never> {
    const id = route.params['invoiceGStLineItemId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invoiceGStLineItem: HttpResponse<IInvoiceGStLineItem>) => {
          if (invoiceGStLineItem.body) {
            return of(invoiceGStLineItem.body);
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
