import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvoiceLineItemComponent } from './list/invoice-line-item.component';
import { InvoiceLineItemDetailComponent } from './detail/invoice-line-item-detail.component';
import { InvoiceLineItemUpdateComponent } from './update/invoice-line-item-update.component';
import { InvoiceLineItemDeleteDialogComponent } from './delete/invoice-line-item-delete-dialog.component';
import { InvoiceLineItemRoutingModule } from './route/invoice-line-item-routing.module';

@NgModule({
  imports: [SharedModule, InvoiceLineItemRoutingModule],
  declarations: [
    InvoiceLineItemComponent,
    InvoiceLineItemDetailComponent,
    InvoiceLineItemUpdateComponent,
    InvoiceLineItemDeleteDialogComponent,
  ],
})
export class InvoiceLineItemModule {}
