import dayjs from 'dayjs/esm';
import { IAddress } from 'app/entities/address/address.model';
import { IRetailer } from 'app/entities/retailer/retailer.model';

export interface ICustomer {
  customerId: number;
  name?: string | null;
  email?: string | null;
  phone?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  invoiceAddress?: Pick<IAddress, 'id'> | null;
  billingAddress?: Pick<IAddress, 'id'> | null;
  retailers?: Pick<IRetailer, 'retailerId'>[] | null;
}

export type NewCustomer = Omit<ICustomer, 'customerId'> & { customerId: null };
