import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { invoiceId: unknown }> = Partial<Omit<T, 'invoiceId'>> & { invoiceId: T['invoiceId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'invoiceId' | 'createdOn' | 'updatedOn'>;

type InvoiceFormGroupContent = {
  invoiceId: FormControl<InvoiceFormRawValue['invoiceId'] | NewInvoice['invoiceId']>;
  billNo: FormControl<InvoiceFormRawValue['billNo']>;
  taxPercentage: FormControl<InvoiceFormRawValue['taxPercentage']>;
  paymentType: FormControl<InvoiceFormRawValue['paymentType']>;
  createdOn: FormControl<InvoiceFormRawValue['createdOn']>;
  updatedOn: FormControl<InvoiceFormRawValue['updatedOn']>;
  createdBy: FormControl<InvoiceFormRawValue['createdBy']>;
  updatedBy: FormControl<InvoiceFormRawValue['updatedBy']>;
  retailers: FormControl<InvoiceFormRawValue['retailers']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { invoiceId: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      invoiceId: new FormControl(
        { value: invoiceRawValue.invoiceId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      billNo: new FormControl(invoiceRawValue.billNo),
      taxPercentage: new FormControl(invoiceRawValue.taxPercentage),
      paymentType: new FormControl(invoiceRawValue.paymentType),
      createdOn: new FormControl(invoiceRawValue.createdOn),
      updatedOn: new FormControl(invoiceRawValue.updatedOn),
      createdBy: new FormControl(invoiceRawValue.createdBy),
      updatedBy: new FormControl(invoiceRawValue.updatedBy),
      retailers: new FormControl(invoiceRawValue.retailers),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        invoiceId: { value: invoiceRawValue.invoiceId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      invoiceId: null,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      createdOn: dayjs(rawInvoice.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawInvoice.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults)
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      createdOn: invoice.createdOn ? invoice.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: invoice.updatedOn ? invoice.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
