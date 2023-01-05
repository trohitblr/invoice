import dayjs from 'dayjs/esm';

import { UserType } from 'app/entities/enumerations/user-type.model';

import { IRetailerUser, NewRetailerUser } from './retailer-user.model';

export const sampleWithRequiredData: IRetailerUser = {
  retailerUserId: 71384,
};

export const sampleWithPartialData: IRetailerUser = {
  retailerUserId: 28396,
  phone: 88619,
  email: 'Loma14@yahoo.com',
  encPassword: 'Borders Nepalese',
  createdOn: dayjs('2023-01-03T01:42'),
  updatedOn: dayjs('2023-01-02T11:18'),
  createdBy: 'Upgradable generate',
  updatedBy: 'withdrawal',
};

export const sampleWithFullData: IRetailerUser = {
  retailerUserId: 11234,
  userId: 'Avon',
  phone: 9398,
  email: 'Sally13@yahoo.com',
  type: UserType['OWNER'],
  encPassword: 'purple Granite needs-based',
  status: false,
  createdOn: dayjs('2023-01-02T22:16'),
  updatedOn: dayjs('2023-01-02T16:03'),
  createdBy: 'Avon Mouse',
  updatedBy: 'Afghanistan',
};

export const sampleWithNewData: NewRetailerUser = {
  retailerUserId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
