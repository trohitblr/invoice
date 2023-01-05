import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetailInventory } from '../retail-inventory.model';
import { RetailInventoryService } from '../service/retail-inventory.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './retail-inventory-delete-dialog.component.html',
})
export class RetailInventoryDeleteDialogComponent {
  retailInventory?: IRetailInventory;

  constructor(protected retailInventoryService: RetailInventoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.retailInventoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
