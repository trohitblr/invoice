import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IRetailer, NewRetailer } from './retailer.model';

export const sampleWithRequiredData: IRetailer = {
  retailerId: 89604,
  name: 'sexy',
};

export const sampleWithPartialData: IRetailer = {
  retailerId: 19930,
  name: 'generating',
  email: 'h$@K(6@SS',
  gstNumber: 'schemas',
  status: Status['INACTIVE'],
  updatedOn: dayjs('2023-01-02T13:59'),
  createdBy: 'Orchestrator',
};

export const sampleWithFullData: IRetailer = {
  retailerId: 28635,
  name: 'COM',
  owner: 'Minnesota',
  phone: 10,
  email: '$WE@SSS',
  gstNumber: 'Internal Ameliorated Concrete',
  status: Status['INACTIVE'],
  createdOn: dayjs('2023-01-02T07:41'),
  updatedOn: dayjs('2023-01-02T22:23'),
  createdBy: 'Concrete',
  updatedBy: 'FTP Bedfordshire',
};

export const sampleWithNewData: NewRetailer = {
  name: 'Kwanza Team-oriented',
  retailerId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
