import dayjs from 'dayjs/esm';

import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 88754,
};

export const sampleWithPartialData: IAddress = {
  id: 34407,
  line1: 'efficient Montana',
  line2: 'interfaces Intelligent Ameliorated',
  pincode: 79117,
  lat: 80702,
  lon: 23466,
  country: 'French Guiana',
  shippingAddress: false,
  billingAddress: false,
  createdOn: dayjs('2023-01-03T01:22'),
  updatedOn: dayjs('2023-01-02T23:29'),
  createdBy: 'Unbranded Handcrafted',
  updatedBy: 'FTP',
};

export const sampleWithFullData: IAddress = {
  id: 82451,
  line1: 'Account',
  line2: 'Incredible hack navigate',
  city: 'Peoria',
  state: 'Italy',
  pincode: 83372,
  lat: 24154,
  lon: 72362,
  country: 'Spain',
  landmark: 'Compatible black maximize',
  shippingAddress: true,
  billingAddress: false,
  homeAddress: true,
  officeAddress: true,
  createdOn: dayjs('2023-01-02T19:07'),
  updatedOn: dayjs('2023-01-02T15:20'),
  createdBy: 'haptic Hill',
  updatedBy: 'Consultant',
};

export const sampleWithNewData: NewAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
