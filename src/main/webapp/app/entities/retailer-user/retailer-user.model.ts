import dayjs from 'dayjs/esm';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { UserType } from 'app/entities/enumerations/user-type.model';

export interface IRetailerUser {
  retailerUserId: number;
  userId?: string | null;
  phone?: number | null;
  email?: string | null;
  type?: UserType | null;
  encPassword?: string | null;
  status?: boolean | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  retailers?: Pick<IRetailer, 'retailerId'> | null;
}

export type NewRetailerUser = Omit<IRetailerUser, 'retailerUserId'> & { retailerUserId: null };
