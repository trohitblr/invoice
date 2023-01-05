import dayjs from 'dayjs/esm';

import { PaymentType } from 'app/entities/enumerations/payment-type.model';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  invoiceId: 91509,
};

export const sampleWithPartialData: IInvoice = {
  invoiceId: 18841,
  billNo: 'Berkshire Germany',
  taxPercentage: 'Venezuela Arkansas auxiliary',
};

export const sampleWithFullData: IInvoice = {
  invoiceId: 55119,
  billNo: 'empower Buckinghamshire Florida',
  taxPercentage: 'Rubber',
  paymentType: PaymentType['CREDIT'],
  createdOn: dayjs('2023-01-02T21:47'),
  updatedOn: dayjs('2023-01-02T08:33'),
  createdBy: 'programming yellow transition',
  updatedBy: 'Soft',
};

export const sampleWithNewData: NewInvoice = {
  invoiceId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
