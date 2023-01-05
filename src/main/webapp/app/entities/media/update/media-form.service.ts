import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMedia, NewMedia } from '../media.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { mediaId: unknown }> = Partial<Omit<T, 'mediaId'>> & { mediaId: T['mediaId'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedia for edit and NewMediaFormGroupInput for create.
 */
type MediaFormGroupInput = IMedia | PartialWithRequiredKeyOf<NewMedia>;

type MediaFormDefaults = Pick<NewMedia, 'mediaId'>;

type MediaFormGroupContent = {
  mediaId: FormControl<IMedia['mediaId'] | NewMedia['mediaId']>;
  path: FormControl<IMedia['path']>;
  type: FormControl<IMedia['type']>;
};

export type MediaFormGroup = FormGroup<MediaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MediaFormService {
  createMediaFormGroup(media: MediaFormGroupInput = { mediaId: null }): MediaFormGroup {
    const mediaRawValue = {
      ...this.getFormDefaults(),
      ...media,
    };
    return new FormGroup<MediaFormGroupContent>({
      mediaId: new FormControl(
        { value: mediaRawValue.mediaId, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      path: new FormControl(mediaRawValue.path, {
        validators: [Validators.required],
      }),
      type: new FormControl(mediaRawValue.type),
    });
  }

  getMedia(form: MediaFormGroup): IMedia | NewMedia {
    return form.getRawValue() as IMedia | NewMedia;
  }

  resetForm(form: MediaFormGroup, media: MediaFormGroupInput): void {
    const mediaRawValue = { ...this.getFormDefaults(), ...media };
    form.reset(
      {
        ...mediaRawValue,
        mediaId: { value: mediaRawValue.mediaId, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MediaFormDefaults {
    return {
      mediaId: null,
    };
  }
}
