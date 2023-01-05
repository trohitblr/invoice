import dayjs from 'dayjs/esm';
import { ICategories } from 'app/entities/categories/categories.model';
import { IGstSlave } from 'app/entities/gst-slave/gst-slave.model';

export interface ICatalouge {
  catalougeId: number;
  name?: string | null;
  cost?: number | null;
  status?: boolean | null;
  description?: string | null;
  hsnNo?: number | null;
  quantity?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  categorie?: Pick<ICategories, 'categoryId'> | null;
  taxSlaves?: Pick<IGstSlave, 'gstSlaveId'> | null;
}

export type NewCatalouge = Omit<ICatalouge, 'catalougeId'> & { catalougeId: null };
