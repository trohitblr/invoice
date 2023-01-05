import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GstSlaveComponent } from '../list/gst-slave.component';
import { GstSlaveDetailComponent } from '../detail/gst-slave-detail.component';
import { GstSlaveUpdateComponent } from '../update/gst-slave-update.component';
import { GstSlaveRoutingResolveService } from './gst-slave-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gstSlaveRoute: Routes = [
  {
    path: '',
    component: GstSlaveComponent,
    data: {
      defaultSort: 'gstSlaveId,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':gstSlaveId/view',
    component: GstSlaveDetailComponent,
    resolve: {
      gstSlave: GstSlaveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GstSlaveUpdateComponent,
    resolve: {
      gstSlave: GstSlaveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':gstSlaveId/edit',
    component: GstSlaveUpdateComponent,
    resolve: {
      gstSlave: GstSlaveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gstSlaveRoute)],
  exports: [RouterModule],
})
export class GstSlaveRoutingModule {}
