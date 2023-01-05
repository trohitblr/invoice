import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoiceLineItem, NewInvoiceLineItem } from '../invoice-line-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { invoiceLineItemId: unknown }> = Partial<Omit<T, 'invoiceLineItemId'>> & {
  invoiceLineItemId: T['invoiceLineItemId'];
};

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceLineItem for edit and NewInvoiceLineItemFormGroupInput for create.
 */
type InvoiceLineItemFormGroupInput = IInvoiceLineItem | PartialWithRequiredKeyOf<NewInvoiceLineItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoiceLineItem | NewInvoiceLineItem> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type InvoiceLineItemFormRawValue = FormValueOf<IInvoiceLineItem>;

type NewInvoiceLineItemFormRawValue = FormValueOf<NewInvoiceLineItem>;

type InvoiceLineItemFormDefaults = Pick<NewInvoiceLineItem, 'invoiceLineItemId' | 'createdOn' | 'updatedOn'>;

type InvoiceLineItemFormGroupContent = {
  invoiceLineItemId: FormControl<InvoiceLineItemFormRawValue['invoiceLineItemId'] | NewInvoiceLineItem['invoiceLineItemId']>;
  articleType: FormControl<InvoiceLineItemFormRawValue['articleType']>;
  articleId: FormControl<InvoiceLineItemFormRawValue['articleId']>;
  quantity: FormControl<InvoiceLineItemFormRawValue['quantity']>;
  amount: FormControl<InvoiceLineItemFormRawValue['amount']>;
  discount: FormControl<InvoiceLineItemFormRawValue['discount']>;
  createdOn: FormControl<InvoiceLineItemFormRawValue['createdOn']>;
  updatedOn: FormControl<InvoiceLineItemFormRawValue['updatedOn']>;
  createdBy: FormControl<InvoiceLineItemFormRawValue['createdBy']>;
  updatedBy: FormControl<InvoiceLineItemFormRawValue['updatedBy']>;
  invoices: FormControl<InvoiceLineItemFormRawValue['invoices']>;
};

export type InvoiceLineItemFormGroup = FormGroup<InvoiceLineItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineItemFormService {
  createInvoiceLineItemFormGroup(invoiceLineItem: InvoiceLineItemFormGroupInput = { invoiceLineItemId: null }): InvoiceLineItemFormGroup {
    const invoiceLineItemRawValue = this.convertInvoiceLineItemToInvoiceLineItemRawValue({
      ...this.getFormDefaults(),
      ...invoiceLineItem,
    });
    return new FormGroup<InvoiceLineItemFormGroupContent>({
      invoiceLineItemId: new FormControl(
        { value: invoiceLineItemRawValue.invoiceLineItemId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      articleType: new FormControl(invoiceLineItemRawValue.articleType),
      articleId: new FormControl(invoiceLineItemRawValue.articleId),
      quantity: new FormControl(invoiceLineItemRawValue.quantity),
      amount: new FormControl(invoiceLineItemRawValue.amount),
      discount: new FormControl(invoiceLineItemRawValue.discount),
      createdOn: new FormControl(invoiceLineItemRawValue.createdOn),
      updatedOn: new FormControl(invoiceLineItemRawValue.updatedOn),
      createdBy: new FormControl(invoiceLineItemRawValue.createdBy),
      updatedBy: new FormControl(invoiceLineItemRawValue.updatedBy),
      invoices: new FormControl(invoiceLineItemRawValue.invoices),
    });
  }

  getInvoiceLineItem(form: InvoiceLineItemFormGroup): IInvoiceLineItem | NewInvoiceLineItem {
    return this.convertInvoiceLineItemRawValueToInvoiceLineItem(
      form.getRawValue() as InvoiceLineItemFormRawValue | NewInvoiceLineItemFormRawValue
    );
  }

  resetForm(form: InvoiceLineItemFormGroup, invoiceLineItem: InvoiceLineItemFormGroupInput): void {
    const invoiceLineItemRawValue = this.convertInvoiceLineItemToInvoiceLineItemRawValue({ ...this.getFormDefaults(), ...invoiceLineItem });
    form.reset(
      {
        ...invoiceLineItemRawValue,
        invoiceLineItemId: { value: invoiceLineItemRawValue.invoiceLineItemId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceLineItemFormDefaults {
    const currentTime = dayjs();

    return {
      invoiceLineItemId: null,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertInvoiceLineItemRawValueToInvoiceLineItem(
    rawInvoiceLineItem: InvoiceLineItemFormRawValue | NewInvoiceLineItemFormRawValue
  ): IInvoiceLineItem | NewInvoiceLineItem {
    return {
      ...rawInvoiceLineItem,
      createdOn: dayjs(rawInvoiceLineItem.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawInvoiceLineItem.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceLineItemToInvoiceLineItemRawValue(
    invoiceLineItem: IInvoiceLineItem | (Partial<NewInvoiceLineItem> & InvoiceLineItemFormDefaults)
  ): InvoiceLineItemFormRawValue | PartialWithRequiredKeyOf<NewInvoiceLineItemFormRawValue> {
    return {
      ...invoiceLineItem,
      createdOn: invoiceLineItem.createdOn ? invoiceLineItem.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: invoiceLineItem.updatedOn ? invoiceLineItem.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
