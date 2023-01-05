import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  paymentTypeValues = Object.keys(PaymentType);

  retailersSharedCollection: IRetailer[] = [];

  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  constructor(
    protected invoiceService: InvoiceService,
    protected invoiceFormService: InvoiceFormService,
    protected retailerService: RetailerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRetailer = (o1: IRetailer | null, o2: IRetailer | null): boolean => this.retailerService.compareRetailer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.invoiceId !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.retailersSharedCollection = this.retailerService.addRetailerToCollectionIfMissing<IRetailer>(
      this.retailersSharedCollection,
      invoice.retailers
    );
  }

  protected loadRelationshipsOptions(): void {
    this.retailerService
      .query()
      .pipe(map((res: HttpResponse<IRetailer[]>) => res.body ?? []))
      .pipe(
        map((retailers: IRetailer[]) =>
          this.retailerService.addRetailerToCollectionIfMissing<IRetailer>(retailers, this.invoice?.retailers)
        )
      )
      .subscribe((retailers: IRetailer[]) => (this.retailersSharedCollection = retailers));
  }
}
