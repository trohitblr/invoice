import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceGStLineItem } from '../invoice-g-st-line-item.model';

@Component({
  selector: 'jhi-invoice-g-st-line-item-detail',
  templateUrl: './invoice-g-st-line-item-detail.component.html',
})
export class InvoiceGStLineItemDetailComponent implements OnInit {
  invoiceGStLineItem: IInvoiceGStLineItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceGStLineItem }) => {
      this.invoiceGStLineItem = invoiceGStLineItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
