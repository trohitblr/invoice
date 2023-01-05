import dayjs from 'dayjs/esm';
import { IAddress } from 'app/entities/address/address.model';
import { IMedia } from 'app/entities/media/media.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IRetailer {
  retailerId: number;
  name?: string | null;
  owner?: string | null;
  phone?: number | null;
  email?: string | null;
  gstNumber?: string | null;
  status?: Status | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  invoiceAddress?: Pick<IAddress, 'id'> | null;
  billingAddress?: Pick<IAddress, 'id'> | null;
  logo?: Pick<IMedia, 'mediaId'> | null;
  customers?: Pick<ICustomer, 'customerId'>[] | null;
}

export type NewRetailer = Omit<IRetailer, 'retailerId'> & { retailerId: null };
