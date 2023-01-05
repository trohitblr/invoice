import dayjs from 'dayjs/esm';

export interface ICategories {
  categoryId: number;
  categoryName?: string | null;
  description?: string | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
}

export type NewCategories = Omit<ICategories, 'categoryId'> & { categoryId: null };
