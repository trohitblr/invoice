import dayjs from 'dayjs/esm';

import { ICatalouge, NewCatalouge } from './catalouge.model';

export const sampleWithRequiredData: ICatalouge = {
  catalougeId: 73852,
};

export const sampleWithPartialData: ICatalouge = {
  catalougeId: 46842,
  name: 'Interface',
  cost: 67599,
  status: false,
  description: 'deposit transmitter synergies',
  hsnNo: 55441,
  quantity: 95290,
  createdOn: dayjs('2023-01-02T10:01'),
  updatedOn: dayjs('2023-01-02T17:53'),
};

export const sampleWithFullData: ICatalouge = {
  catalougeId: 21113,
  name: 'Metal',
  cost: 78674,
  status: false,
  description: 'Colorado',
  hsnNo: 55265,
  quantity: 3107,
  createdOn: dayjs('2023-01-02T10:09'),
  updatedOn: dayjs('2023-01-03T06:14'),
  createdBy: 'Alaska',
  updatedBy: 'Account Architect',
};

export const sampleWithNewData: NewCatalouge = {
  catalougeId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
