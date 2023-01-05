import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RetailInventoryFormService, RetailInventoryFormGroup } from './retail-inventory-form.service';
import { IRetailInventory } from '../retail-inventory.model';
import { RetailInventoryService } from '../service/retail-inventory.service';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';

@Component({
  selector: 'jhi-retail-inventory-update',
  templateUrl: './retail-inventory-update.component.html',
})
export class RetailInventoryUpdateComponent implements OnInit {
  isSaving = false;
  retailInventory: IRetailInventory | null = null;

  retailersSharedCollection: IRetailer[] = [];

  editForm: RetailInventoryFormGroup = this.retailInventoryFormService.createRetailInventoryFormGroup();

  constructor(
    protected retailInventoryService: RetailInventoryService,
    protected retailInventoryFormService: RetailInventoryFormService,
    protected retailerService: RetailerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRetailer = (o1: IRetailer | null, o2: IRetailer | null): boolean => this.retailerService.compareRetailer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailInventory }) => {
      this.retailInventory = retailInventory;
      if (retailInventory) {
        this.updateForm(retailInventory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retailInventory = this.retailInventoryFormService.getRetailInventory(this.editForm);
    if (retailInventory.retailInventoryId !== null) {
      this.subscribeToSaveResponse(this.retailInventoryService.update(retailInventory));
    } else {
      this.subscribeToSaveResponse(this.retailInventoryService.create(retailInventory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetailInventory>>): void {
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

  protected updateForm(retailInventory: IRetailInventory): void {
    this.retailInventory = retailInventory;
    this.retailInventoryFormService.resetForm(this.editForm, retailInventory);

    this.retailersSharedCollection = this.retailerService.addRetailerToCollectionIfMissing<IRetailer>(
      this.retailersSharedCollection,
      retailInventory.catalougs
    );
  }

  protected loadRelationshipsOptions(): void {
    this.retailerService
      .query()
      .pipe(map((res: HttpResponse<IRetailer[]>) => res.body ?? []))
      .pipe(
        map((retailers: IRetailer[]) =>
          this.retailerService.addRetailerToCollectionIfMissing<IRetailer>(retailers, this.retailInventory?.catalougs)
        )
      )
      .subscribe((retailers: IRetailer[]) => (this.retailersSharedCollection = retailers));
  }
}
