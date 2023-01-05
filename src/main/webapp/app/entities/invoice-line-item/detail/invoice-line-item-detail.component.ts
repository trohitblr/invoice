import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceLineItem } from '../invoice-line-item.model';

@Component({
  selector: 'jhi-invoice-line-item-detail',
  templateUrl: './invoice-line-item-detail.component.html',
})
export class InvoiceLineItemDetailComponent implements OnInit {
  invoiceLineItem: IInvoiceLineItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceLineItem }) => {
      this.invoiceLineItem = invoiceLineItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
