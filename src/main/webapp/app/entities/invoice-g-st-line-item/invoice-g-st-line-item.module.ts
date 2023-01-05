import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvoiceGStLineItemComponent } from './list/invoice-g-st-line-item.component';
import { InvoiceGStLineItemDetailComponent } from './detail/invoice-g-st-line-item-detail.component';
import { InvoiceGStLineItemUpdateComponent } from './update/invoice-g-st-line-item-update.component';
import { InvoiceGStLineItemDeleteDialogComponent } from './delete/invoice-g-st-line-item-delete-dialog.component';
import { InvoiceGStLineItemRoutingModule } from './route/invoice-g-st-line-item-routing.module';

@NgModule({
  imports: [SharedModule, InvoiceGStLineItemRoutingModule],
  declarations: [
    InvoiceGStLineItemComponent,
    InvoiceGStLineItemDetailComponent,
    InvoiceGStLineItemUpdateComponent,
    InvoiceGStLineItemDeleteDialogComponent,
  ],
})
export class InvoiceGStLineItemModule {}
