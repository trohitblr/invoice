import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GstSlaveComponent } from './list/gst-slave.component';
import { GstSlaveDetailComponent } from './detail/gst-slave-detail.component';
import { GstSlaveUpdateComponent } from './update/gst-slave-update.component';
import { GstSlaveDeleteDialogComponent } from './delete/gst-slave-delete-dialog.component';
import { GstSlaveRoutingModule } from './route/gst-slave-routing.module';

@NgModule({
  imports: [SharedModule, GstSlaveRoutingModule],
  declarations: [GstSlaveComponent, GstSlaveDetailComponent, GstSlaveUpdateComponent, GstSlaveDeleteDialogComponent],
})
export class GstSlaveModule {}
