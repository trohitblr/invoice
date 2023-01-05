import { GstType } from 'app/entities/enumerations/gst-type.model';

import { IGstSlave, NewGstSlave } from './gst-slave.model';

export const sampleWithRequiredData: IGstSlave = {
  gstSlaveId: 33657,
};

export const sampleWithPartialData: IGstSlave = {
  gstSlaveId: 38157,
};

export const sampleWithFullData: IGstSlave = {
  gstSlaveId: 96815,
  tax: GstType['CGSTSGST'],
  taxPercentage: 31869,
};

export const sampleWithNewData: NewGstSlave = {
  gstSlaveId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
