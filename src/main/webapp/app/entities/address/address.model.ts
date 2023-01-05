import dayjs from 'dayjs/esm';

export interface IAddress {
  id: number;
  line1?: string | null;
  line2?: string | null;
  city?: string | null;
  state?: string | null;
  pincode?: number | null;
  lat?: number | null;
  lon?: number | null;
  country?: string | null;
  landmark?: string | null;
  shippingAddress?: boolean | null;
  billingAddress?: boolean | null;
  homeAddress?: boolean | null;
  officeAddress?: boolean | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
