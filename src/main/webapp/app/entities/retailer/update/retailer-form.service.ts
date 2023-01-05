import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRetailer, NewRetailer } from '../retailer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { retailerId: unknown }> = Partial<Omit<T, 'retailerId'>> & { retailerId: T['retailerId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRetailer for edit and NewRetailerFormGroupInput for create.
 */
type RetailerFormGroupInput = IRetailer | PartialWithRequiredKeyOf<NewRetailer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRetailer | NewRetailer> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type RetailerFormRawValue = FormValueOf<IRetailer>;

type NewRetailerFormRawValue = FormValueOf<NewRetailer>;

type RetailerFormDefaults = Pick<NewRetailer, 'retailerId' | 'createdOn' | 'updatedOn' | 'customers'>;

type RetailerFormGroupContent = {
  retailerId: FormControl<RetailerFormRawValue['retailerId'] | NewRetailer['retailerId']>;
  name: FormControl<RetailerFormRawValue['name']>;
  owner: FormControl<RetailerFormRawValue['owner']>;
  phone: FormControl<RetailerFormRawValue['phone']>;
  email: FormControl<RetailerFormRawValue['email']>;
  gstNumber: FormControl<RetailerFormRawValue['gstNumber']>;
  status: FormControl<RetailerFormRawValue['status']>;
  createdOn: FormControl<RetailerFormRawValue['createdOn']>;
  updatedOn: FormControl<RetailerFormRawValue['updatedOn']>;
  createdBy: FormControl<RetailerFormRawValue['createdBy']>;
  updatedBy: FormControl<RetailerFormRawValue['updatedBy']>;
  invoiceAddress: FormControl<RetailerFormRawValue['invoiceAddress']>;
  billingAddress: FormControl<RetailerFormRawValue['billingAddress']>;
  logo: FormControl<RetailerFormRawValue['logo']>;
  customers: FormControl<RetailerFormRawValue['customers']>;
};

export type RetailerFormGroup = FormGroup<RetailerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RetailerFormService {
  createRetailerFormGroup(retailer: RetailerFormGroupInput = { retailerId: null }): RetailerFormGroup {
    const retailerRawValue = this.convertRetailerToRetailerRawValue({
      ...this.getFormDefaults(),
      ...retailer,
    });
    return new FormGroup<RetailerFormGroupContent>({
      retailerId: new FormControl(
        { value: retailerRawValue.retailerId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(retailerRawValue.name, {
        validators: [Validators.required],
      }),
      owner: new FormControl(retailerRawValue.owner),
      phone: new FormControl(retailerRawValue.phone, {
        validators: [Validators.min(10), Validators.max(10)],
      }),
      email: new FormControl(retailerRawValue.email, {
        validators: [Validators.pattern('^(.+)@(\\\\S+)$')],
      }),
      gstNumber: new FormControl(retailerRawValue.gstNumber),
      status: new FormControl(retailerRawValue.status),
      createdOn: new FormControl(retailerRawValue.createdOn),
      updatedOn: new FormControl(retailerRawValue.updatedOn),
      createdBy: new FormControl(retailerRawValue.createdBy),
      updatedBy: new FormControl(retailerRawValue.updatedBy),
      invoiceAddress: new FormControl(retailerRawValue.invoiceAddress),
      billingAddress: new FormControl(retailerRawValue.billingAddress),
      logo: new FormControl(retailerRawValue.logo),
      customers: new FormControl(retailerRawValue.customers ?? []),
    });
  }

  getRetailer(form: RetailerFormGroup): IRetailer | NewRetailer {
    return this.convertRetailerRawValueToRetailer(form.getRawValue() as RetailerFormRawValue | NewRetailerFormRawValue);
  }

  resetForm(form: RetailerFormGroup, retailer: RetailerFormGroupInput): void {
    const retailerRawValue = this.convertRetailerToRetailerRawValue({ ...this.getFormDefaults(), ...retailer });
    form.reset(
      {
        ...retailerRawValue,
        retailerId: { value: retailerRawValue.retailerId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RetailerFormDefaults {
    const currentTime = dayjs();

    return {
      retailerId: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      customers: [],
    };
  }

  private convertRetailerRawValueToRetailer(rawRetailer: RetailerFormRawValue | NewRetailerFormRawValue): IRetailer | NewRetailer {
    return {
      ...rawRetailer,
      createdOn: dayjs(rawRetailer.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawRetailer.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertRetailerToRetailerRawValue(
    retailer: IRetailer | (Partial<NewRetailer> & RetailerFormDefaults)
  ): RetailerFormRawValue | PartialWithRequiredKeyOf<NewRetailerFormRawValue> {
    return {
      ...retailer,
      createdOn: retailer.createdOn ? retailer.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: retailer.updatedOn ? retailer.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      customers: retailer.customers ?? [],
    };
  }
}
