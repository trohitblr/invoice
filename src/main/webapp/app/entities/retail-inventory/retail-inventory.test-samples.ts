import dayjs from 'dayjs/esm';

import { IRetailInventory, NewRetailInventory } from './retail-inventory.model';

export const sampleWithRequiredData: IRetailInventory = {
  retailInventoryId: 87031,
};

export const sampleWithPartialData: IRetailInventory = {
  retailInventoryId: 90592,
  quantity: 76579,
  soldQty: 35601,
  minLimit: 28590,
  status: false,
  createdOn: dayjs('2023-01-03T02:08'),
  updatedOn: dayjs('2023-01-02T18:14'),
  createdBy: 'Colorado',
  updatedBy: 'Account',
};

export const sampleWithFullData: IRetailInventory = {
  retailInventoryId: 19347,
  quantity: 12163,
  availableQty: 1787,
  soldQty: 66230,
  maxLimit: 69266,
  minLimit: 33111,
  status: true,
  createdOn: dayjs('2023-01-03T06:08'),
  updatedOn: dayjs('2023-01-02T23:20'),
  createdBy: 'systematic Frozen Italy',
  updatedBy: 'Principal',
};

export const sampleWithNewData: NewRetailInventory = {
  retailInventoryId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
