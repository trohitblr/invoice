import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalouge } from '../catalouge.model';
import { CatalougeService } from '../service/catalouge.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './catalouge-delete-dialog.component.html',
})
export class CatalougeDeleteDialogComponent {
  catalouge?: ICatalouge;

  constructor(protected catalougeService: CatalougeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalougeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
