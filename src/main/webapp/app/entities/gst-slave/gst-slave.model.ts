import { GstType } from 'app/entities/enumerations/gst-type.model';

export interface IGstSlave {
  gstSlaveId: number;
  tax?: GstType | null;
  taxPercentage?: number | null;
}

export type NewGstSlave = Omit<IGstSlave, 'gstSlaveId'> & { gstSlaveId: null };
