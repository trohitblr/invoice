import dayjs from 'dayjs/esm';
import { IRetailer } from 'app/entities/retailer/retailer.model';

export interface IRetailInventory {
  retailInventoryId: number;
  quantity?: number | null;
  availableQty?: number | null;
  soldQty?: number | null;
  maxLimit?: number | null;
  minLimit?: number | null;
  status?: boolean | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  catalougs?: Pick<IRetailer, 'retailerId'> | null;
}

export type NewRetailInventory = Omit<IRetailInventory, 'retailInventoryId'> & { retailInventoryId: null };
