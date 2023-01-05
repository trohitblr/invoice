import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGstSlave } from '../gst-slave.model';
import { GstSlaveService } from '../service/gst-slave.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './gst-slave-delete-dialog.component.html',
})
export class GstSlaveDeleteDialogComponent {
  gstSlave?: IGstSlave;

  constructor(protected gstSlaveService: GstSlaveService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gstSlaveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
