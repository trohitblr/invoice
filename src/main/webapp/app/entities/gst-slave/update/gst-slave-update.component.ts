import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GstSlaveFormService, GstSlaveFormGroup } from './gst-slave-form.service';
import { IGstSlave } from '../gst-slave.model';
import { GstSlaveService } from '../service/gst-slave.service';
import { GstType } from 'app/entities/enumerations/gst-type.model';

@Component({
  selector: 'jhi-gst-slave-update',
  templateUrl: './gst-slave-update.component.html',
})
export class GstSlaveUpdateComponent implements OnInit {
  isSaving = false;
  gstSlave: IGstSlave | null = null;
  gstTypeValues = Object.keys(GstType);

  editForm: GstSlaveFormGroup = this.gstSlaveFormService.createGstSlaveFormGroup();

  constructor(
    protected gstSlaveService: GstSlaveService,
    protected gstSlaveFormService: GstSlaveFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gstSlave }) => {
      this.gstSlave = gstSlave;
      if (gstSlave) {
        this.updateForm(gstSlave);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gstSlave = this.gstSlaveFormService.getGstSlave(this.editForm);
    if (gstSlave.gstSlaveId !== null) {
      this.subscribeToSaveResponse(this.gstSlaveService.update(gstSlave));
    } else {
      this.subscribeToSaveResponse(this.gstSlaveService.create(gstSlave));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGstSlave>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gstSlave: IGstSlave): void {
    this.gstSlave = gstSlave;
    this.gstSlaveFormService.resetForm(this.editForm, gstSlave);
  }
}
