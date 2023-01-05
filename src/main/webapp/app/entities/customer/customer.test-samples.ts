import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  customerId: 24379,
  phone: 10,
};

export const sampleWithPartialData: ICustomer = {
  customerId: 88729,
  name: 'Gorgeous generate',
  email: 'uOpW@SS',
  phone: 10,
  createdBy: 'invoice',
  updatedBy: 'Cotton payment',
};

export const sampleWithFullData: ICustomer = {
  customerId: 26718,
  name: 'input',
  email: 'BWP%@SSSS',
  phone: 10,
  createdOn: dayjs('2023-01-02T16:34'),
  updatedOn: dayjs('2023-01-02T11:28'),
  createdBy: 'intangible Balanced',
  updatedBy: 'extranet seize',
};

export const sampleWithNewData: NewCustomer = {
  phone: 10,
  customerId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
