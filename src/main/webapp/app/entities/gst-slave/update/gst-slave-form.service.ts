import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGstSlave, NewGstSlave } from '../gst-slave.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { gstSlaveId: unknown }> = Partial<Omit<T, 'gstSlaveId'>> & { gstSlaveId: T['gstSlaveId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGstSlave for edit and NewGstSlaveFormGroupInput for create.
 */
type GstSlaveFormGroupInput = IGstSlave | PartialWithRequiredKeyOf<NewGstSlave>;

type GstSlaveFormDefaults = Pick<NewGstSlave, 'gstSlaveId'>;

type GstSlaveFormGroupContent = {
  gstSlaveId: FormControl<IGstSlave['gstSlaveId'] | NewGstSlave['gstSlaveId']>;
  tax: FormControl<IGstSlave['tax']>;
  taxPercentage: FormControl<IGstSlave['taxPercentage']>;
};

export type GstSlaveFormGroup = FormGroup<GstSlaveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GstSlaveFormService {
  createGstSlaveFormGroup(gstSlave: GstSlaveFormGroupInput = { gstSlaveId: null }): GstSlaveFormGroup {
    const gstSlaveRawValue = {
      ...this.getFormDefaults(),
      ...gstSlave,
    };
    return new FormGroup<GstSlaveFormGroupContent>({
      gstSlaveId: new FormControl(
        { value: gstSlaveRawValue.gstSlaveId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tax: new FormControl(gstSlaveRawValue.tax),
      taxPercentage: new FormControl(gstSlaveRawValue.taxPercentage),
    });
  }

  getGstSlave(form: GstSlaveFormGroup): IGstSlave | NewGstSlave {
    return form.getRawValue() as IGstSlave | NewGstSlave;
  }

  resetForm(form: GstSlaveFormGroup, gstSlave: GstSlaveFormGroupInput): void {
    const gstSlaveRawValue = { ...this.getFormDefaults(), ...gstSlave };
    form.reset(
      {
        ...gstSlaveRawValue,
        gstSlaveId: { value: gstSlaveRawValue.gstSlaveId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GstSlaveFormDefaults {
    return {
      gstSlaveId: null,
    };
  }
}
