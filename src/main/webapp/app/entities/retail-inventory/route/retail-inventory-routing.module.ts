import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RetailInventoryComponent } from '../list/retail-inventory.component';
import { RetailInventoryDetailComponent } from '../detail/retail-inventory-detail.component';
import { RetailInventoryUpdateComponent } from '../update/retail-inventory-update.component';
import { RetailInventoryRoutingResolveService } from './retail-inventory-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const retailInventoryRoute: Routes = [
  {
    path: '',
    component: RetailInventoryComponent,
    data: {
      defaultSort: 'retailInventoryId,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':retailInventoryId/view',
    component: RetailInventoryDetailComponent,
    resolve: {
      retailInventory: RetailInventoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RetailInventoryUpdateComponent,
    resolve: {
      retailInventory: RetailInventoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':retailInventoryId/edit',
    component: RetailInventoryUpdateComponent,
    resolve: {
      retailInventory: RetailInventoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(retailInventoryRoute)],
  exports: [RouterModule],
})
export class RetailInventoryRoutingModule {}
