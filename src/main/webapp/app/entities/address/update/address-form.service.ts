import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAddress | NewAddress> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type AddressFormRawValue = FormValueOf<IAddress>;

type NewAddressFormRawValue = FormValueOf<NewAddress>;

type AddressFormDefaults = Pick<
  NewAddress,
  'id' | 'shippingAddress' | 'billingAddress' | 'homeAddress' | 'officeAddress' | 'createdOn' | 'updatedOn'
>;

type AddressFormGroupContent = {
  id: FormControl<AddressFormRawValue['id'] | NewAddress['id']>;
  line1: FormControl<AddressFormRawValue['line1']>;
  line2: FormControl<AddressFormRawValue['line2']>;
  city: FormControl<AddressFormRawValue['city']>;
  state: FormControl<AddressFormRawValue['state']>;
  pincode: FormControl<AddressFormRawValue['pincode']>;
  lat: FormControl<AddressFormRawValue['lat']>;
  lon: FormControl<AddressFormRawValue['lon']>;
  country: FormControl<AddressFormRawValue['country']>;
  landmark: FormControl<AddressFormRawValue['landmark']>;
  shippingAddress: FormControl<AddressFormRawValue['shippingAddress']>;
  billingAddress: FormControl<AddressFormRawValue['billingAddress']>;
  homeAddress: FormControl<AddressFormRawValue['homeAddress']>;
  officeAddress: FormControl<AddressFormRawValue['officeAddress']>;
  createdOn: FormControl<AddressFormRawValue['createdOn']>;
  updatedOn: FormControl<AddressFormRawValue['updatedOn']>;
  createdBy: FormControl<AddressFormRawValue['createdBy']>;
  updatedBy: FormControl<AddressFormRawValue['updatedBy']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = this.convertAddressToAddressRawValue({
      ...this.getFormDefaults(),
      ...address,
    });
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      line1: new FormControl(addressRawValue.line1),
      line2: new FormControl(addressRawValue.line2),
      city: new FormControl(addressRawValue.city),
      state: new FormControl(addressRawValue.state),
      pincode: new FormControl(addressRawValue.pincode),
      lat: new FormControl(addressRawValue.lat),
      lon: new FormControl(addressRawValue.lon),
      country: new FormControl(addressRawValue.country),
      landmark: new FormControl(addressRawValue.landmark),
      shippingAddress: new FormControl(addressRawValue.shippingAddress),
      billingAddress: new FormControl(addressRawValue.billingAddress),
      homeAddress: new FormControl(addressRawValue.homeAddress),
      officeAddress: new FormControl(addressRawValue.officeAddress),
      createdOn: new FormControl(addressRawValue.createdOn),
      updatedOn: new FormControl(addressRawValue.updatedOn),
      createdBy: new FormControl(addressRawValue.createdBy),
      updatedBy: new FormControl(addressRawValue.updatedBy),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return this.convertAddressRawValueToAddress(form.getRawValue() as AddressFormRawValue | NewAddressFormRawValue);
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = this.convertAddressToAddressRawValue({ ...this.getFormDefaults(), ...address });
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      shippingAddress: false,
      billingAddress: false,
      homeAddress: false,
      officeAddress: false,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertAddressRawValueToAddress(rawAddress: AddressFormRawValue | NewAddressFormRawValue): IAddress | NewAddress {
    return {
      ...rawAddress,
      createdOn: dayjs(rawAddress.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawAddress.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertAddressToAddressRawValue(
    address: IAddress | (Partial<NewAddress> & AddressFormDefaults)
  ): AddressFormRawValue | PartialWithRequiredKeyOf<NewAddressFormRawValue> {
    return {
      ...address,
      createdOn: address.createdOn ? address.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: address.updatedOn ? address.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
