import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RetailerUserComponent } from './list/retailer-user.component';
import { RetailerUserDetailComponent } from './detail/retailer-user-detail.component';
import { RetailerUserUpdateComponent } from './update/retailer-user-update.component';
import { RetailerUserDeleteDialogComponent } from './delete/retailer-user-delete-dialog.component';
import { RetailerUserRoutingModule } from './route/retailer-user-routing.module';

@NgModule({
  imports: [SharedModule, RetailerUserRoutingModule],
  declarations: [RetailerUserComponent, RetailerUserDetailComponent, RetailerUserUpdateComponent, RetailerUserDeleteDialogComponent],
})
export class RetailerUserModule {}
