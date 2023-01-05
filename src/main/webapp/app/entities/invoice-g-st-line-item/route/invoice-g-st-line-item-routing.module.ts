import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceGStLineItemComponent } from '../list/invoice-g-st-line-item.component';
import { InvoiceGStLineItemDetailComponent } from '../detail/invoice-g-st-line-item-detail.component';
import { InvoiceGStLineItemUpdateComponent } from '../update/invoice-g-st-line-item-update.component';
import { InvoiceGStLineItemRoutingResolveService } from './invoice-g-st-line-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const invoiceGStLineItemRoute: Routes = [
  {
    path: '',
    component: InvoiceGStLineItemComponent,
    data: {
      defaultSort: 'invoiceGStLineItemId,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':invoiceGStLineItemId/view',
    component: InvoiceGStLineItemDetailComponent,
    resolve: {
      invoiceGStLineItem: InvoiceGStLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceGStLineItemUpdateComponent,
    resolve: {
      invoiceGStLineItem: InvoiceGStLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':invoiceGStLineItemId/edit',
    component: InvoiceGStLineItemUpdateComponent,
    resolve: {
      invoiceGStLineItem: InvoiceGStLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invoiceGStLineItemRoute)],
  exports: [RouterModule],
})
export class InvoiceGStLineItemRoutingModule {}
