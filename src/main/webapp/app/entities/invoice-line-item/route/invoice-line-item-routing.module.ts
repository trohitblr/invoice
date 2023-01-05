import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceLineItemComponent } from '../list/invoice-line-item.component';
import { InvoiceLineItemDetailComponent } from '../detail/invoice-line-item-detail.component';
import { InvoiceLineItemUpdateComponent } from '../update/invoice-line-item-update.component';
import { InvoiceLineItemRoutingResolveService } from './invoice-line-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const invoiceLineItemRoute: Routes = [
  {
    path: '',
    component: InvoiceLineItemComponent,
    data: {
      defaultSort: 'invoiceLineItemId,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':invoiceLineItemId/view',
    component: InvoiceLineItemDetailComponent,
    resolve: {
      invoiceLineItem: InvoiceLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceLineItemUpdateComponent,
    resolve: {
      invoiceLineItem: InvoiceLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':invoiceLineItemId/edit',
    component: InvoiceLineItemUpdateComponent,
    resolve: {
      invoiceLineItem: InvoiceLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invoiceLineItemRoute)],
  exports: [RouterModule],
})
export class InvoiceLineItemRoutingModule {}
