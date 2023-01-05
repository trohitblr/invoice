import dayjs from 'dayjs/esm';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ArticleType } from 'app/entities/enumerations/article-type.model';

export interface IInvoiceGStLineItem {
  invoiceGStLineItemId: number;
  articleType?: ArticleType | null;
  articleId?: string | null;
  hsnsac?: string | null;
  sgst?: number | null;
  cgst?: number | null;
  sgstTaxAmount?: number | null;
  cgstTaxAmount?: number | null;
  amount?: number | null;
  discount?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  invoices?: Pick<IInvoice, 'invoiceId'> | null;
}

export type NewInvoiceGStLineItem = Omit<IInvoiceGStLineItem, 'invoiceGStLineItemId'> & { invoiceGStLineItemId: null };
