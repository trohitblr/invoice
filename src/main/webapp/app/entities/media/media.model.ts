import { MediaType } from 'app/entities/enumerations/media-type.model';

export interface IMedia {
  mediaId: number;
  path?: string | null;
  type?: MediaType | null;
}

export type NewMedia = Omit<IMedia, 'mediaId'> & { mediaId: null };
