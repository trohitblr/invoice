import dayjs from 'dayjs/esm';

import { ArticleType } from 'app/entities/enumerations/article-type.model';

import { IInvoiceLineItem, NewInvoiceLineItem } from './invoice-line-item.model';

export const sampleWithRequiredData: IInvoiceLineItem = {
  invoiceLineItemId: 72600,
};

export const sampleWithPartialData: IInvoiceLineItem = {
  invoiceLineItemId: 18892,
  articleType: ArticleType['AIR'],
  quantity: 93684,
  updatedOn: dayjs('2023-01-03T02:12'),
  createdBy: 'Small Function-based',
  updatedBy: 'technologies emulation infrastructures',
};

export const sampleWithFullData: IInvoiceLineItem = {
  invoiceLineItemId: 82476,
  articleType: ArticleType['AIR'],
  articleId: 'Kip',
  quantity: 9675,
  amount: 92606,
  discount: 66742,
  createdOn: dayjs('2023-01-03T05:25'),
  updatedOn: dayjs('2023-01-02T07:12'),
  createdBy: 'Pike Pants matrix',
  updatedBy: 'SMS Licensed compelling',
};

export const sampleWithNewData: NewInvoiceLineItem = {
  invoiceLineItemId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
