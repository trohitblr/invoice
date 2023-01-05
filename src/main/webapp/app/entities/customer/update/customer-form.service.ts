import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { customerId: unknown }> = Partial<Omit<T, 'customerId'>> & { customerId: T['customerId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomer | NewCustomer> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type CustomerFormRawValue = FormValueOf<ICustomer>;

type NewCustomerFormRawValue = FormValueOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'customerId' | 'createdOn' | 'updatedOn' | 'retailers'>;

type CustomerFormGroupContent = {
  customerId: FormControl<CustomerFormRawValue['customerId'] | NewCustomer['customerId']>;
  name: FormControl<CustomerFormRawValue['name']>;
  email: FormControl<CustomerFormRawValue['email']>;
  phone: FormControl<CustomerFormRawValue['phone']>;
  createdOn: FormControl<CustomerFormRawValue['createdOn']>;
  updatedOn: FormControl<CustomerFormRawValue['updatedOn']>;
  createdBy: FormControl<CustomerFormRawValue['createdBy']>;
  updatedBy: FormControl<CustomerFormRawValue['updatedBy']>;
  invoiceAddress: FormControl<CustomerFormRawValue['invoiceAddress']>;
  billingAddress: FormControl<CustomerFormRawValue['billingAddress']>;
  retailers: FormControl<CustomerFormRawValue['retailers']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { customerId: null }): CustomerFormGroup {
    const customerRawValue = this.convertCustomerToCustomerRawValue({
      ...this.getFormDefaults(),
      ...customer,
    });
    return new FormGroup<CustomerFormGroupContent>({
      customerId: new FormControl(
        { value: customerRawValue.customerId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(customerRawValue.name),
      email: new FormControl(customerRawValue.email, {
        validators: [Validators.pattern('^(.+)@(\\\\S+)$')],
      }),
      phone: new FormControl(customerRawValue.phone, {
        validators: [Validators.required, Validators.min(10), Validators.max(10)],
      }),
      createdOn: new FormControl(customerRawValue.createdOn),
      updatedOn: new FormControl(customerRawValue.updatedOn),
      createdBy: new FormControl(customerRawValue.createdBy),
      updatedBy: new FormControl(customerRawValue.updatedBy),
      invoiceAddress: new FormControl(customerRawValue.invoiceAddress),
      billingAddress: new FormControl(customerRawValue.billingAddress),
      retailers: new FormControl(customerRawValue.retailers ?? []),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return this.convertCustomerRawValueToCustomer(form.getRawValue() as CustomerFormRawValue | NewCustomerFormRawValue);
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = this.convertCustomerToCustomerRawValue({ ...this.getFormDefaults(), ...customer });
    form.reset(
      {
        ...customerRawValue,
        customerId: { value: customerRawValue.customerId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    const currentTime = dayjs();

    return {
      customerId: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      retailers: [],
    };
  }

  private convertCustomerRawValueToCustomer(rawCustomer: CustomerFormRawValue | NewCustomerFormRawValue): ICustomer | NewCustomer {
    return {
      ...rawCustomer,
      createdOn: dayjs(rawCustomer.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawCustomer.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerToCustomerRawValue(
    customer: ICustomer | (Partial<NewCustomer> & CustomerFormDefaults)
  ): CustomerFormRawValue | PartialWithRequiredKeyOf<NewCustomerFormRawValue> {
    return {
      ...customer,
      createdOn: customer.createdOn ? customer.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: customer.updatedOn ? customer.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      retailers: customer.retailers ?? [],
    };
  }
}
