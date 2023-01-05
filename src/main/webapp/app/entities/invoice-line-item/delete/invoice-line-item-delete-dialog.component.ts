import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceLineItem } from '../invoice-line-item.model';
import { InvoiceLineItemService } from '../service/invoice-line-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './invoice-line-item-delete-dialog.component.html',
})
export class InvoiceLineItemDeleteDialogComponent {
  invoiceLineItem?: IInvoiceLineItem;

  constructor(protected invoiceLineItemService: InvoiceLineItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceLineItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
