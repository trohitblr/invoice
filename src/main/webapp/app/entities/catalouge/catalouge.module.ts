import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalougeComponent } from './list/catalouge.component';
import { CatalougeDetailComponent } from './detail/catalouge-detail.component';
import { CatalougeUpdateComponent } from './update/catalouge-update.component';
import { CatalougeDeleteDialogComponent } from './delete/catalouge-delete-dialog.component';
import { CatalougeRoutingModule } from './route/catalouge-routing.module';

@NgModule({
  imports: [SharedModule, CatalougeRoutingModule],
  declarations: [CatalougeComponent, CatalougeDetailComponent, CatalougeUpdateComponent, CatalougeDeleteDialogComponent],
})
export class CatalougeModule {}
