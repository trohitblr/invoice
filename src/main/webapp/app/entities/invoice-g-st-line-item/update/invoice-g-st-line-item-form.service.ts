import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoiceGStLineItem, NewInvoiceGStLineItem } from '../invoice-g-st-line-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { invoiceGStLineItemId: unknown }> = Partial<Omit<T, 'invoiceGStLineItemId'>> & {
  invoiceGStLineItemId: T['invoiceGStLineItemId'];
};

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceGStLineItem for edit and NewInvoiceGStLineItemFormGroupInput for create.
 */
type InvoiceGStLineItemFormGroupInput = IInvoiceGStLineItem | PartialWithRequiredKeyOf<NewInvoiceGStLineItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoiceGStLineItem | NewInvoiceGStLineItem> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type InvoiceGStLineItemFormRawValue = FormValueOf<IInvoiceGStLineItem>;

type NewInvoiceGStLineItemFormRawValue = FormValueOf<NewInvoiceGStLineItem>;

type InvoiceGStLineItemFormDefaults = Pick<NewInvoiceGStLineItem, 'invoiceGStLineItemId' | 'createdOn' | 'updatedOn'>;

type InvoiceGStLineItemFormGroupContent = {
  invoiceGStLineItemId: FormControl<InvoiceGStLineItemFormRawValue['invoiceGStLineItemId'] | NewInvoiceGStLineItem['invoiceGStLineItemId']>;
  articleType: FormControl<InvoiceGStLineItemFormRawValue['articleType']>;
  articleId: FormControl<InvoiceGStLineItemFormRawValue['articleId']>;
  hsnsac: FormControl<InvoiceGStLineItemFormRawValue['hsnsac']>;
  sgst: FormControl<InvoiceGStLineItemFormRawValue['sgst']>;
  cgst: FormControl<InvoiceGStLineItemFormRawValue['cgst']>;
  sgstTaxAmount: FormControl<InvoiceGStLineItemFormRawValue['sgstTaxAmount']>;
  cgstTaxAmount: FormControl<InvoiceGStLineItemFormRawValue['cgstTaxAmount']>;
  amount: FormControl<InvoiceGStLineItemFormRawValue['amount']>;
  discount: FormControl<InvoiceGStLineItemFormRawValue['discount']>;
  createdOn: FormControl<InvoiceGStLineItemFormRawValue['createdOn']>;
  updatedOn: FormControl<InvoiceGStLineItemFormRawValue['updatedOn']>;
  createdBy: FormControl<InvoiceGStLineItemFormRawValue['createdBy']>;
  updatedBy: FormControl<InvoiceGStLineItemFormRawValue['updatedBy']>;
  invoices: FormControl<InvoiceGStLineItemFormRawValue['invoices']>;
};

export type InvoiceGStLineItemFormGroup = FormGroup<InvoiceGStLineItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceGStLineItemFormService {
  createInvoiceGStLineItemFormGroup(
    invoiceGStLineItem: InvoiceGStLineItemFormGroupInput = { invoiceGStLineItemId: null }
  ): InvoiceGStLineItemFormGroup {
    const invoiceGStLineItemRawValue = this.convertInvoiceGStLineItemToInvoiceGStLineItemRawValue({
      ...this.getFormDefaults(),
      ...invoiceGStLineItem,
    });
    return new FormGroup<InvoiceGStLineItemFormGroupContent>({
      invoiceGStLineItemId: new FormControl(
        { value: invoiceGStLineItemRawValue.invoiceGStLineItemId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      articleType: new FormControl(invoiceGStLineItemRawValue.articleType),
      articleId: new FormControl(invoiceGStLineItemRawValue.articleId),
      hsnsac: new FormControl(invoiceGStLineItemRawValue.hsnsac),
      sgst: new FormControl(invoiceGStLineItemRawValue.sgst),
      cgst: new FormControl(invoiceGStLineItemRawValue.cgst),
      sgstTaxAmount: new FormControl(invoiceGStLineItemRawValue.sgstTaxAmount),
      cgstTaxAmount: new FormControl(invoiceGStLineItemRawValue.cgstTaxAmount),
      amount: new FormControl(invoiceGStLineItemRawValue.amount),
      discount: new FormControl(invoiceGStLineItemRawValue.discount),
      createdOn: new FormControl(invoiceGStLineItemRawValue.createdOn),
      updatedOn: new FormControl(invoiceGStLineItemRawValue.updatedOn),
      createdBy: new FormControl(invoiceGStLineItemRawValue.createdBy),
      updatedBy: new FormControl(invoiceGStLineItemRawValue.updatedBy),
      invoices: new FormControl(invoiceGStLineItemRawValue.invoices),
    });
  }

  getInvoiceGStLineItem(form: InvoiceGStLineItemFormGroup): IInvoiceGStLineItem | NewInvoiceGStLineItem {
    return this.convertInvoiceGStLineItemRawValueToInvoiceGStLineItem(
      form.getRawValue() as InvoiceGStLineItemFormRawValue | NewInvoiceGStLineItemFormRawValue
    );
  }

  resetForm(form: InvoiceGStLineItemFormGroup, invoiceGStLineItem: InvoiceGStLineItemFormGroupInput): void {
    const invoiceGStLineItemRawValue = this.convertInvoiceGStLineItemToInvoiceGStLineItemRawValue({
      ...this.getFormDefaults(),
      ...invoiceGStLineItem,
    });
    form.reset(
      {
        ...invoiceGStLineItemRawValue,
        invoiceGStLineItemId: { value: invoiceGStLineItemRawValue.invoiceGStLineItemId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceGStLineItemFormDefaults {
    const currentTime = dayjs();

    return {
      invoiceGStLineItemId: null,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertInvoiceGStLineItemRawValueToInvoiceGStLineItem(
    rawInvoiceGStLineItem: InvoiceGStLineItemFormRawValue | NewInvoiceGStLineItemFormRawValue
  ): IInvoiceGStLineItem | NewInvoiceGStLineItem {
    return {
      ...rawInvoiceGStLineItem,
      createdOn: dayjs(rawInvoiceGStLineItem.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawInvoiceGStLineItem.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceGStLineItemToInvoiceGStLineItemRawValue(
    invoiceGStLineItem: IInvoiceGStLineItem | (Partial<NewInvoiceGStLineItem> & InvoiceGStLineItemFormDefaults)
  ): InvoiceGStLineItemFormRawValue | PartialWithRequiredKeyOf<NewInvoiceGStLineItemFormRawValue> {
    return {
      ...invoiceGStLineItem,
      createdOn: invoiceGStLineItem.createdOn ? invoiceGStLineItem.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: invoiceGStLineItem.updatedOn ? invoiceGStLineItem.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
