import dayjs from 'dayjs/esm';

import { ICategories, NewCategories } from './categories.model';

export const sampleWithRequiredData: ICategories = {
  categoryId: 71041,
};

export const sampleWithPartialData: ICategories = {
  categoryId: 32364,
  categoryName: 'Granite',
  description: 'driver District',
  updatedOn: dayjs('2023-01-02T21:29'),
  updatedBy: 'pink Mouse',
};

export const sampleWithFullData: ICategories = {
  categoryId: 35689,
  categoryName: 'SQL systemic',
  description: 'Forward',
  createdOn: dayjs('2023-01-02T09:16'),
  updatedOn: dayjs('2023-01-03T04:08'),
  createdBy: 'Soft Libyan Cape',
  updatedBy: 'Towels Utah input',
};

export const sampleWithNewData: NewCategories = {
  categoryId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
