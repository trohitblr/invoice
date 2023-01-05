import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceGStLineItem } from '../invoice-g-st-line-item.model';
import { InvoiceGStLineItemService } from '../service/invoice-g-st-line-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './invoice-g-st-line-item-delete-dialog.component.html',
})
export class InvoiceGStLineItemDeleteDialogComponent {
  invoiceGStLineItem?: IInvoiceGStLineItem;

  constructor(protected invoiceGStLineItemService: InvoiceGStLineItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceGStLineItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
