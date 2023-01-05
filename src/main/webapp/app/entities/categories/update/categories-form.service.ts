import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICategories, NewCategories } from '../categories.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { categoryId: unknown }> = Partial<Omit<T, 'categoryId'>> & { categoryId: T['categoryId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategories for edit and NewCategoriesFormGroupInput for create.
 */
type CategoriesFormGroupInput = ICategories | PartialWithRequiredKeyOf<NewCategories>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICategories | NewCategories> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

type CategoriesFormRawValue = FormValueOf<ICategories>;

type NewCategoriesFormRawValue = FormValueOf<NewCategories>;

type CategoriesFormDefaults = Pick<NewCategories, 'categoryId' | 'createdOn' | 'updatedOn'>;

type CategoriesFormGroupContent = {
  categoryId: FormControl<CategoriesFormRawValue['categoryId'] | NewCategories['categoryId']>;
  categoryName: FormControl<CategoriesFormRawValue['categoryName']>;
  description: FormControl<CategoriesFormRawValue['description']>;
  createdOn: FormControl<CategoriesFormRawValue['createdOn']>;
  updatedOn: FormControl<CategoriesFormRawValue['updatedOn']>;
  createdBy: FormControl<CategoriesFormRawValue['createdBy']>;
  updatedBy: FormControl<CategoriesFormRawValue['updatedBy']>;
};

export type CategoriesFormGroup = FormGroup<CategoriesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriesFormService {
  createCategoriesFormGroup(categories: CategoriesFormGroupInput = { categoryId: null }): CategoriesFormGroup {
    const categoriesRawValue = this.convertCategoriesToCategoriesRawValue({
      ...this.getFormDefaults(),
      ...categories,
    });
    return new FormGroup<CategoriesFormGroupContent>({
      categoryId: new FormControl(
        { value: categoriesRawValue.categoryId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      categoryName: new FormControl(categoriesRawValue.categoryName),
      description: new FormControl(categoriesRawValue.description),
      createdOn: new FormControl(categoriesRawValue.createdOn),
      updatedOn: new FormControl(categoriesRawValue.updatedOn),
      createdBy: new FormControl(categoriesRawValue.createdBy),
      updatedBy: new FormControl(categoriesRawValue.updatedBy),
    });
  }

  getCategories(form: CategoriesFormGroup): ICategories | NewCategories {
    return this.convertCategoriesRawValueToCategories(form.getRawValue() as CategoriesFormRawValue | NewCategoriesFormRawValue);
  }

  resetForm(form: CategoriesFormGroup, categories: CategoriesFormGroupInput): void {
    const categoriesRawValue = this.convertCategoriesToCategoriesRawValue({ ...this.getFormDefaults(), ...categories });
    form.reset(
      {
        ...categoriesRawValue,
        categoryId: { value: categoriesRawValue.categoryId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriesFormDefaults {
    const currentTime = dayjs();

    return {
      categoryId: null,
      createdOn: currentTime,
      updatedOn: currentTime,
    };
  }

  private convertCategoriesRawValueToCategories(
    rawCategories: CategoriesFormRawValue | NewCategoriesFormRawValue
  ): ICategories | NewCategories {
    return {
      ...rawCategories,
      createdOn: dayjs(rawCategories.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawCategories.updatedOn, DATE_TIME_FORMAT),
    };
  }

  private convertCategoriesToCategoriesRawValue(
    categories: ICategories | (Partial<NewCategories> & CategoriesFormDefaults)
  ): CategoriesFormRawValue | PartialWithRequiredKeyOf<NewCategoriesFormRawValue> {
    return {
      ...categories,
      createdOn: categories.createdOn ? categories.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: categories.updatedOn ? categories.updatedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
