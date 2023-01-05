import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalougeComponent } from '../list/catalouge.component';
import { CatalougeDetailComponent } from '../detail/catalouge-detail.component';
import { CatalougeUpdateComponent } from '../update/catalouge-update.component';
import { CatalougeRoutingResolveService } from './catalouge-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const catalougeRoute: Routes = [
  {
    path: '',
    component: CatalougeComponent,
    data: {
      defaultSort: 'catalougeId,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':catalougeId/view',
    component: CatalougeDetailComponent,
    resolve: {
      catalouge: CatalougeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalougeUpdateComponent,
    resolve: {
      catalouge: CatalougeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':catalougeId/edit',
    component: CatalougeUpdateComponent,
    resolve: {
      catalouge: CatalougeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalougeRoute)],
  exports: [RouterModule],
})
export class CatalougeRoutingModule {}
