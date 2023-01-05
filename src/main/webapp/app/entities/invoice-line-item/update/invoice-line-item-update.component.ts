import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InvoiceLineItemFormService, InvoiceLineItemFormGroup } from './invoice-line-item-form.service';
import { IInvoiceLineItem } from '../invoice-line-item.model';
import { InvoiceLineItemService } from '../service/invoice-line-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { ArticleType } from 'app/entities/enumerations/article-type.model';

@Component({
  selector: 'jhi-invoice-line-item-update',
  templateUrl: './invoice-line-item-update.component.html',
})
export class InvoiceLineItemUpdateComponent implements OnInit {
  isSaving = false;
  invoiceLineItem: IInvoiceLineItem | null = null;
  articleTypeValues = Object.keys(ArticleType);

  invoicesSharedCollection: IInvoice[] = [];

  editForm: InvoiceLineItemFormGroup = this.invoiceLineItemFormService.createInvoiceLineItemFormGroup();

  constructor(
    protected invoiceLineItemService: InvoiceLineItemService,
    protected invoiceLineItemFormService: InvoiceLineItemFormService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceLineItem }) => {
      this.invoiceLineItem = invoiceLineItem;
      if (invoiceLineItem) {
        this.updateForm(invoiceLineItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceLineItem = this.invoiceLineItemFormService.getInvoiceLineItem(this.editForm);
    if (invoiceLineItem.invoiceLineItemId !== null) {
      this.subscribeToSaveResponse(this.invoiceLineItemService.update(invoiceLineItem));
    } else {
      this.subscribeToSaveResponse(this.invoiceLineItemService.create(invoiceLineItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceLineItem>>): void {
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

  protected updateForm(invoiceLineItem: IInvoiceLineItem): void {
    this.invoiceLineItem = invoiceLineItem;
    this.invoiceLineItemFormService.resetForm(this.editForm, invoiceLineItem);

    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(
      this.invoicesSharedCollection,
      invoiceLineItem.invoices
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) =>
          this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.invoiceLineItem?.invoices)
        )
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }
}
