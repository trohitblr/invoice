import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceLineItem } from '../invoice-line-item.model';
import { InvoiceLineItemService } from '../service/invoice-line-item.service';

@Injectable({ providedIn: 'root' })
export class InvoiceLineItemRoutingResolveService implements Resolve<IInvoiceLineItem | null> {
  constructor(protected service: InvoiceLineItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceLineItem | null | never> {
    const id = route.params['invoiceLineItemId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invoiceLineItem: HttpResponse<IInvoiceLineItem>) => {
          if (invoiceLineItem.body) {
            return of(invoiceLineItem.body);
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
