import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RetailerFormService, RetailerFormGroup } from './retailer-form.service';
import { IRetailer } from '../retailer.model';
import { RetailerService } from '../service/retailer.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IMedia } from 'app/entities/media/media.model';
import { MediaService } from 'app/entities/media/service/media.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-retailer-update',
  templateUrl: './retailer-update.component.html',
})
export class RetailerUpdateComponent implements OnInit {
  isSaving = false;
  retailer: IRetailer | null = null;
  statusValues = Object.keys(Status);

  addressesSharedCollection: IAddress[] = [];
  mediaSharedCollection: IMedia[] = [];
  customersSharedCollection: ICustomer[] = [];

  editForm: RetailerFormGroup = this.retailerFormService.createRetailerFormGroup();

  constructor(
    protected retailerService: RetailerService,
    protected retailerFormService: RetailerFormService,
    protected addressService: AddressService,
    protected mediaService: MediaService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  compareMedia = (o1: IMedia | null, o2: IMedia | null): boolean => this.mediaService.compareMedia(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailer }) => {
      this.retailer = retailer;
      if (retailer) {
        this.updateForm(retailer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retailer = this.retailerFormService.getRetailer(this.editForm);
    if (retailer.retailerId !== null) {
      this.subscribeToSaveResponse(this.retailerService.update(retailer));
    } else {
      this.subscribeToSaveResponse(this.retailerService.create(retailer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetailer>>): void {
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

  protected updateForm(retailer: IRetailer): void {
    this.retailer = retailer;
    this.retailerFormService.resetForm(this.editForm, retailer);

    this.addressesSharedCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(
      this.addressesSharedCollection,
      retailer.invoiceAddress,
      retailer.billingAddress
    );
    this.mediaSharedCollection = this.mediaService.addMediaToCollectionIfMissing<IMedia>(this.mediaSharedCollection, retailer.logo);
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      ...(retailer.customers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.addressService
      .query()
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) =>
          this.addressService.addAddressToCollectionIfMissing<IAddress>(
            addresses,
            this.retailer?.invoiceAddress,
            this.retailer?.billingAddress
          )
        )
      )
      .subscribe((addresses: IAddress[]) => (this.addressesSharedCollection = addresses));

    this.mediaService
      .query()
      .pipe(map((res: HttpResponse<IMedia[]>) => res.body ?? []))
      .pipe(map((media: IMedia[]) => this.mediaService.addMediaToCollectionIfMissing<IMedia>(media, this.retailer?.logo)))
      .subscribe((media: IMedia[]) => (this.mediaSharedCollection = media));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, ...(this.retailer?.customers ?? []))
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
