import { MediaType } from 'app/entities/enumerations/media-type.model';

import { IMedia, NewMedia } from './media.model';

export const sampleWithRequiredData: IMedia = {
  mediaId: 70740,
  path: 'COM Creative',
};

export const sampleWithPartialData: IMedia = {
  mediaId: 18045,
  path: 'deposit withdrawal red',
};

export const sampleWithFullData: IMedia = {
  mediaId: 41083,
  path: 'Automotive',
  type: MediaType['JEPG'],
};

export const sampleWithNewData: NewMedia = {
  path: 'orange Chair interfaces',
  mediaId: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
