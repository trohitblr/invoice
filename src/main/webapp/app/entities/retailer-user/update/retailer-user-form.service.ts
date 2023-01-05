import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRetailerUser, NewRetailerUser } from '../retailer-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { retailerUserId: unknown }> = Partial<Omit<T, 'retailerUserId'>> & {
  retailerUserId: T['retailerUserId'];
};

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRetailerUser for edit and NewRetailerUserFormGroupInput for create.
 */
type RetailerUserFormGroupInput = IRetailerUser | PartialWithRequiredKeyOf<NewRetailerUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRetailerUser | NewRetailerUser> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type RetailerUserFormRawValue = FormValueOf<IRetailerUser>;

type NewRetailerUserFormRawValue = FormValueOf<NewRetailerUser>;

type RetailerUserFormDefaults = Pick<NewRetailerUser, 'retailerUserId' | 'status' | 'createdOn' | 'updatedOn'>;

type RetailerUserFormGroupContent = {
  retailerUserId: FormControl<RetailerUserFormRawValue['retailerUserId'] | NewRetailerUser['retailerUserId']>;
  userId: FormControl<RetailerUserFormRawValue['userId']>;
  phone: FormControl<RetailerUserFormRawValue['phone']>;
  email: FormControl<RetailerUserFormRawValue['email']>;
  type: FormControl<RetailerUserFormRawValue['type']>;
  encPassword: FormControl<RetailerUserFormRawValue['encPassword']>;
  status: FormControl<RetailerUserFormRawValue['status']>;
  createdOn: FormControl<RetailerUserFormRawValue['createdOn']>;
  updatedOn: FormControl<RetailerUserFormRawValue['updatedOn']>;
  createdBy: FormControl<RetailerUserFormRawValue['createdBy']>;
  updatedBy: FormControl<RetailerUserFormRawValue['updatedBy']>;
  retailers: FormControl<RetailerUserFormRawValue['retailers']>;
};

export type RetailerUserFormGroup = FormGroup<RetailerUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RetailerUserFormService {
  createRetailerUserFormGroup(retailerUser: RetailerUserFormGroupInput = { retailerUserId: null }): RetailerUserFormGroup {
    const retailerUserRawValue = this.convertRetailerUserToRetailerUserRawValue({
      ...this.getFormDefaults(),
      ...retailerUser,
    });
    return new FormGroup<RetailerUserFormGroupContent>({
      retailerUserId: new FormControl(
        { value: retailerUserRawValue.retailerUserId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userId: new FormControl(retailerUserRawValue.userId),
      phone: new FormControl(retailerUserRawValue.phone),
      email: new FormControl(retailerUserRawValue.email),
      type: new FormControl(retailerUserRawValue.type),
      encPassword: new FormControl(retailerUserRawValue.encPassword),
      status: new FormControl(retailerUserRawValue.status),
      createdOn: new FormControl(retailerUserRawValue.createdOn),
      updatedOn: new FormControl(retailerUserRawValue.updatedOn),
      createdBy: new FormControl(retailerUserRawValue.createdBy),
      updatedBy: new FormControl(retailerUserRawValue.updatedBy),
      retailers: new FormControl(retailerUserRawValue.retailers),
    });
  }

  getRetailerUser(form: RetailerUserFormGroup): IRetailerUser | NewRetailerUser {
    return this.convertRetailerUserRawValueToRetailerUser(form.getRawValue() as RetailerUserFormRawValue | NewRetailerUserFormRawValue);
  }

  resetForm(form: RetailerUserFormGroup, retailerUser: RetailerUserFormGroupInput): void {
    const retailerUserRawValue = this.convertRetailerUserToRetailerUserRawValue({ ...this.getFormDefaults(), ...retailerUser });
    form.reset(
      {
        ...retailerUserRawValue,
        retailerUserId: { value: retailerUserRawValue.retailerUserId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RetailerUserFormDefaults {
    const currentTime = dayjs();

    return {
      retailerUserId: null,
      status: false,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertRetailerUserRawValueToRetailerUser(
    rawRetailerUser: RetailerUserFormRawValue | NewRetailerUserFormRawValue
  ): IRetailerUser | NewRetailerUser {
    return {
      ...rawRetailerUser,
      createdOn: dayjs(rawRetailerUser.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawRetailerUser.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertRetailerUserToRetailerUserRawValue(
    retailerUser: IRetailerUser | (Partial<NewRetailerUser> & RetailerUserFormDefaults)
  ): RetailerUserFormRawValue | PartialWithRequiredKeyOf<NewRetailerUserFormRawValue> {
    return {
      ...retailerUser,
      createdOn: retailerUser.createdOn ? retailerUser.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: retailerUser.updatedOn ? retailerUser.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
