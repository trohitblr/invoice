import dayjs from 'dayjs/esm';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ArticleType } from 'app/entities/enumerations/article-type.model';

export interface IInvoiceLineItem {
  invoiceLineItemId: number;
  articleType?: ArticleType | null;
  articleId?: string | null;
  quantity?: number | null;
  amount?: number | null;
  discount?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  invoices?: Pick<IInvoice, 'invoiceId'> | null;
}

export type NewInvoiceLineItem = Omit<IInvoiceLineItem, 'invoiceLineItemId'> & { invoiceLineItemId: null };
