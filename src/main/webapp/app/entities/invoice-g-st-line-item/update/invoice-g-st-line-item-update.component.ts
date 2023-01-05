import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InvoiceGStLineItemFormService, InvoiceGStLineItemFormGroup } from './invoice-g-st-line-item-form.service';
import { IInvoiceGStLineItem } from '../invoice-g-st-line-item.model';
import { InvoiceGStLineItemService } from '../service/invoice-g-st-line-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { ArticleType } from 'app/entities/enumerations/article-type.model';

@Component({
  selector: 'jhi-invoice-g-st-line-item-update',
  templateUrl: './invoice-g-st-line-item-update.component.html',
})
export class InvoiceGStLineItemUpdateComponent implements OnInit {
  isSaving = false;
  invoiceGStLineItem: IInvoiceGStLineItem | null = null;
  articleTypeValues = Object.keys(ArticleType);

  invoicesSharedCollection: IInvoice[] = [];

  editForm: InvoiceGStLineItemFormGroup = this.invoiceGStLineItemFormService.createInvoiceGStLineItemFormGroup();

  constructor(
    protected invoiceGStLineItemService: InvoiceGStLineItemService,
    protected invoiceGStLineItemFormService: InvoiceGStLineItemFormService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceGStLineItem }) => {
      this.invoiceGStLineItem = invoiceGStLineItem;
      if (invoiceGStLineItem) {
        this.updateForm(invoiceGStLineItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceGStLineItem = this.invoiceGStLineItemFormService.getInvoiceGStLineItem(this.editForm);
    if (invoiceGStLineItem.invoiceGStLineItemId !== null) {
      this.subscribeToSaveResponse(this.invoiceGStLineItemService.update(invoiceGStLineItem));
    } else {
      this.subscribeToSaveResponse(this.invoiceGStLineItemService.create(invoiceGStLineItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceGStLineItem>>): void {
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

  protected updateForm(invoiceGStLineItem: IInvoiceGStLineItem): void {
    this.invoiceGStLineItem = invoiceGStLineItem;
    this.invoiceGStLineItemFormService.resetForm(this.editForm, invoiceGStLineItem);

    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(
      this.invoicesSharedCollection,
      invoiceGStLineItem.invoices
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) =>
          this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.invoiceGStLineItem?.invoices)
        )
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }
}
