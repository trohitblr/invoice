import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGstSlave } from '../gst-slave.model';
import { GstSlaveService } from '../service/gst-slave.service';

@Injectable({ providedIn: 'root' })
export class GstSlaveRoutingResolveService implements Resolve<IGstSlave | null> {
  constructor(protected service: GstSlaveService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGstSlave | null | never> {
    const id = route.params['gstSlaveId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gstSlave: HttpResponse<IGstSlave>) => {
          if (gstSlave.body) {
            return of(gstSlave.body);
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
