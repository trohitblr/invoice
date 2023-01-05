import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICatalouge, NewCatalouge } from '../catalouge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { catalougeId: unknown }> = Partial<Omit<T, 'catalougeId'>> & { catalougeId: T['catalougeId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalouge for edit and NewCatalougeFormGroupInput for create.
 */
type CatalougeFormGroupInput = ICatalouge | PartialWithRequiredKeyOf<NewCatalouge>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICatalouge | NewCatalouge> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type CatalougeFormRawValue = FormValueOf<ICatalouge>;

type NewCatalougeFormRawValue = FormValueOf<NewCatalouge>;

type CatalougeFormDefaults = Pick<NewCatalouge, 'catalougeId' | 'status' | 'createdOn' | 'updatedOn'>;

type CatalougeFormGroupContent = {
  catalougeId: FormControl<CatalougeFormRawValue['catalougeId'] | NewCatalouge['catalougeId']>;
  name: FormControl<CatalougeFormRawValue['name']>;
  cost: FormControl<CatalougeFormRawValue['cost']>;
  status: FormControl<CatalougeFormRawValue['status']>;
  description: FormControl<CatalougeFormRawValue['description']>;
  hsnNo: FormControl<CatalougeFormRawValue['hsnNo']>;
  quantity: FormControl<CatalougeFormRawValue['quantity']>;
  createdOn: FormControl<CatalougeFormRawValue['createdOn']>;
  updatedOn: FormControl<CatalougeFormRawValue['updatedOn']>;
  createdBy: FormControl<CatalougeFormRawValue['createdBy']>;
  updatedBy: FormControl<CatalougeFormRawValue['updatedBy']>;
  categorie: FormControl<CatalougeFormRawValue['categorie']>;
  taxSlaves: FormControl<CatalougeFormRawValue['taxSlaves']>;
};

export type CatalougeFormGroup = FormGroup<CatalougeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CatalougeFormService {
  createCatalougeFormGroup(catalouge: CatalougeFormGroupInput = { catalougeId: null }): CatalougeFormGroup {
    const catalougeRawValue = this.convertCatalougeToCatalougeRawValue({
      ...this.getFormDefaults(),
      ...catalouge,
    });
    return new FormGroup<CatalougeFormGroupContent>({
      catalougeId: new FormControl(
        { value: catalougeRawValue.catalougeId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(catalougeRawValue.name),
      cost: new FormControl(catalougeRawValue.cost),
      status: new FormControl(catalougeRawValue.status),
      description: new FormControl(catalougeRawValue.description),
      hsnNo: new FormControl(catalougeRawValue.hsnNo),
      quantity: new FormControl(catalougeRawValue.quantity),
      createdOn: new FormControl(catalougeRawValue.createdOn),
      updatedOn: new FormControl(catalougeRawValue.updatedOn),
      createdBy: new FormControl(catalougeRawValue.createdBy),
      updatedBy: new FormControl(catalougeRawValue.updatedBy),
      categorie: new FormControl(catalougeRawValue.categorie),
      taxSlaves: new FormControl(catalougeRawValue.taxSlaves),
    });
  }

  getCatalouge(form: CatalougeFormGroup): ICatalouge | NewCatalouge {
    return this.convertCatalougeRawValueToCatalouge(form.getRawValue() as CatalougeFormRawValue | NewCatalougeFormRawValue);
  }

  resetForm(form: CatalougeFormGroup, catalouge: CatalougeFormGroupInput): void {
    const catalougeRawValue = this.convertCatalougeToCatalougeRawValue({ ...this.getFormDefaults(), ...catalouge });
    form.reset(
      {
        ...catalougeRawValue,
        catalougeId: { value: catalougeRawValue.catalougeId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CatalougeFormDefaults {
    const currentTime = dayjs();

    return {
      catalougeId: null,
      status: false,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertCatalougeRawValueToCatalouge(rawCatalouge: CatalougeFormRawValue | NewCatalougeFormRawValue): ICatalouge | NewCatalouge {
    return {
      ...rawCatalouge,
      createdOn: dayjs(rawCatalouge.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawCatalouge.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertCatalougeToCatalougeRawValue(
    catalouge: ICatalouge | (Partial<NewCatalouge> & CatalougeFormDefaults)
  ): CatalougeFormRawValue | PartialWithRequiredKeyOf<NewCatalougeFormRawValue> {
    return {
      ...catalouge,
      createdOn: catalouge.createdOn ? catalouge.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: catalouge.updatedOn ? catalouge.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
