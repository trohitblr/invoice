import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RetailerUserComponent } from '../list/retailer-user.component';
import { RetailerUserDetailComponent } from '../detail/retailer-user-detail.component';
import { RetailerUserUpdateComponent } from '../update/retailer-user-update.component';
import { RetailerUserRoutingResolveService } from './retailer-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const retailerUserRoute: Routes = [
  {
    path: '',
    component: RetailerUserComponent,
    data: {
      defaultSort: 'retailerUserId,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':retailerUserId/view',
    component: RetailerUserDetailComponent,
    resolve: {
      retailerUser: RetailerUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RetailerUserUpdateComponent,
    resolve: {
      retailerUser: RetailerUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':retailerUserId/edit',
    component: RetailerUserUpdateComponent,
    resolve: {
      retailerUser: RetailerUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(retailerUserRoute)],
  exports: [RouterModule],
})
export class RetailerUserRoutingModule {}
