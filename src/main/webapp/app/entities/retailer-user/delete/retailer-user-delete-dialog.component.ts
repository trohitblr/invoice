import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetailerUser } from '../retailer-user.model';
import { RetailerUserService } from '../service/retailer-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './retailer-user-delete-dialog.component.html',
})
export class RetailerUserDeleteDialogComponent {
  retailerUser?: IRetailerUser;

  constructor(protected retailerUserService: RetailerUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.retailerUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
