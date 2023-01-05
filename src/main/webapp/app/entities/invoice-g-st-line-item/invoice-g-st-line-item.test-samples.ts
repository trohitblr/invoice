import dayjs from 'dayjs/esm';

import { ArticleType } from 'app/entities/enumerations/article-type.model';

import { IInvoiceGStLineItem, NewInvoiceGStLineItem } from './invoice-g-st-line-item.model';

export const sampleWithRequiredData: IInvoiceGStLineItem = {
  invoiceGStLineItemId: 64962,
};

export const sampleWithPartialData: IInvoiceGStLineItem = {
  invoiceGStLineItemId: 51782,
  sgst: 10329,
  cgst: 10300,
  amount: 16568,
  discount: 23172,
  createdOn: dayjs('2023-01-03T05:08'),
  updatedOn: dayjs('2023-01-03T03:07'),
};

export const sampleWithFullData: IInvoiceGStLineItem = {
  invoiceGStLineItemId: 43430,
  articleType: ArticleType['AIR'],
  articleId: 'transmit',
  hsnsac: 'Engineer',
  sgst: 26423,
  cgst: 11839,
  sgstTaxAmount: 59917,
  cgstTaxAmount: 38552,
  amount: 9943,
  discount: 54973,
  createdOn: dayjs('2023-01-03T00:10'),
  updatedOn: dayjs('2023-01-02T19:18'),
  createdBy: 'Customer Marketing Future',
  updatedBy: 'Berkshire Barbados',
};

export const sampleWithNewData: NewInvoiceGStLineItem = {
  invoiceGStLineItemId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
