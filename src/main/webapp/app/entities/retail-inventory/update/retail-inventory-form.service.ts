import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRetailInventory, NewRetailInventory } from '../retail-inventory.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { retailInventoryId: unknown }> = Partial<Omit<T, 'retailInventoryId'>> & {
  retailInventoryId: T['retailInventoryId'];
};

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRetailInventory for edit and NewRetailInventoryFormGroupInput for create.
 */
type RetailInventoryFormGroupInput = IRetailInventory | PartialWithRequiredKeyOf<NewRetailInventory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRetailInventory | NewRetailInventory> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type RetailInventoryFormRawValue = FormValueOf<IRetailInventory>;

type NewRetailInventoryFormRawValue = FormValueOf<NewRetailInventory>;

type RetailInventoryFormDefaults = Pick<NewRetailInventory, 'retailInventoryId' | 'status' | 'createdOn' | 'updatedOn'>;

type RetailInventoryFormGroupContent = {
  retailInventoryId: FormControl<RetailInventoryFormRawValue['retailInventoryId'] | NewRetailInventory['retailInventoryId']>;
  quantity: FormControl<RetailInventoryFormRawValue['quantity']>;
  availableQty: FormControl<RetailInventoryFormRawValue['availableQty']>;
  soldQty: FormControl<RetailInventoryFormRawValue['soldQty']>;
  maxLimit: FormControl<RetailInventoryFormRawValue['maxLimit']>;
  minLimit: FormControl<RetailInventoryFormRawValue['minLimit']>;
  status: FormControl<RetailInventoryFormRawValue['status']>;
  createdOn: FormControl<RetailInventoryFormRawValue['createdOn']>;
  updatedOn: FormControl<RetailInventoryFormRawValue['updatedOn']>;
  createdBy: FormControl<RetailInventoryFormRawValue['createdBy']>;
  updatedBy: FormControl<RetailInventoryFormRawValue['updatedBy']>;
  catalougs: FormControl<RetailInventoryFormRawValue['catalougs']>;
};

export type RetailInventoryFormGroup = FormGroup<RetailInventoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RetailInventoryFormService {
  createRetailInventoryFormGroup(retailInventory: RetailInventoryFormGroupInput = { retailInventoryId: null }): RetailInventoryFormGroup {
    const retailInventoryRawValue = this.convertRetailInventoryToRetailInventoryRawValue({
      ...this.getFormDefaults(),
      ...retailInventory,
    });
    return new FormGroup<RetailInventoryFormGroupContent>({
      retailInventoryId: new FormControl(
        { value: retailInventoryRawValue.retailInventoryId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      quantity: new FormControl(retailInventoryRawValue.quantity),
      availableQty: new FormControl(retailInventoryRawValue.availableQty),
      soldQty: new FormControl(retailInventoryRawValue.soldQty),
      maxLimit: new FormControl(retailInventoryRawValue.maxLimit),
      minLimit: new FormControl(retailInventoryRawValue.minLimit),
      status: new FormControl(retailInventoryRawValue.status),
      createdOn: new FormControl(retailInventoryRawValue.createdOn),
      updatedOn: new FormControl(retailInventoryRawValue.updatedOn),
      createdBy: new FormControl(retailInventoryRawValue.createdBy),
      updatedBy: new FormControl(retailInventoryRawValue.updatedBy),
      catalougs: new FormControl(retailInventoryRawValue.catalougs),
    });
  }

  getRetailInventory(form: RetailInventoryFormGroup): IRetailInventory | NewRetailInventory {
    return this.convertRetailInventoryRawValueToRetailInventory(
      form.getRawValue() as RetailInventoryFormRawValue | NewRetailInventoryFormRawValue
    );
  }

  resetForm(form: RetailInventoryFormGroup, retailInventory: RetailInventoryFormGroupInput): void {
    const retailInventoryRawValue = this.convertRetailInventoryToRetailInventoryRawValue({ ...this.getFormDefaults(), ...retailInventory });
    form.reset(
      {
        ...retailInventoryRawValue,
        retailInventoryId: { value: retailInventoryRawValue.retailInventoryId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RetailInventoryFormDefaults {
    const currentTime = dayjs();

    return {
      retailInventoryId: null,
      status: false,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertRetailInventoryRawValueToRetailInventory(
    rawRetailInventory: RetailInventoryFormRawValue | NewRetailInventoryFormRawValue
  ): IRetailInventory | NewRetailInventory {
    return {
      ...rawRetailInventory,
      createdOn: dayjs(rawRetailInventory.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawRetailInventory.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertRetailInventoryToRetailInventoryRawValue(
    retailInventory: IRetailInventory | (Partial<NewRetailInventory> & RetailInventoryFormDefaults)
  ): RetailInventoryFormRawValue | PartialWithRequiredKeyOf<NewRetailInventoryFormRawValue> {
    return {
      ...retailInventory,
      createdOn: retailInventory.createdOn ? retailInventory.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: retailInventory.updatedOn ? retailInventory.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
