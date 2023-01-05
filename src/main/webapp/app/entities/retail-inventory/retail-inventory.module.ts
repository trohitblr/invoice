import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RetailInventoryComponent } from './list/retail-inventory.component';
import { RetailInventoryDetailComponent } from './detail/retail-inventory-detail.component';
import { RetailInventoryUpdateComponent } from './update/retail-inventory-update.component';
import { RetailInventoryDeleteDialogComponent } from './delete/retail-inventory-delete-dialog.component';
import { RetailInventoryRoutingModule } from './route/retail-inventory-routing.module';

@NgModule({
  imports: [SharedModule, RetailInventoryRoutingModule],
  declarations: [
    RetailInventoryComponent,
    RetailInventoryDetailComponent,
    RetailInventoryUpdateComponent,
    RetailInventoryDeleteDialogComponent,
  ],
})
export class RetailInventoryModule {}
