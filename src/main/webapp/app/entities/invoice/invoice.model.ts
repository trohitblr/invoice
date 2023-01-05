import dayjs from 'dayjs/esm';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';

export interface IInvoice {
  invoiceId: number;
  billNo?: string | null;
  taxPercentage?: string | null;
  paymentType?: PaymentType | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  retailers?: Pick<IRetailer, 'retailerId'> | null;
}

export type NewInvoice = Omit<IInvoice, 'invoiceId'> & { invoiceId: null };
