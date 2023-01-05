import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../media.test-samples';

import { MediaFormService } from './media-form.service';

describe('Media Form Service', () => {
  let service: MediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MediaFormService);
  });

  describe('Service methods', () => {
    describe('createMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            mediaId: expect.any(Object),
            path: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing IMedia should create a new form with FormGroup', () => {
        const formGroup = service.createMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            mediaId: expect.any(Object),
            path: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getMedia', () => {
      it('should return NewMedia for default Media initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMediaFormGroup(sampleWithNewData);

        const media = service.getMedia(formGroup) as any;

        expect(media).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedia for empty Media initial value', () => {
        const formGroup = service.createMediaFormGroup();

        const media = service.getMedia(formGroup) as any;

        expect(media).toMatchObject({});
      });

      it('should return IMedia', () => {
        const formGroup = service.createMediaFormGroup(sampleWithRequiredData);

        const media = service.getMedia(formGroup) as any;

        expect(media).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedia should not enable mediaId FormControl', () => {
        const formGroup = service.createMediaFormGroup();
        expect(formGroup.controls.mediaId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.mediaId.disabled).toBe(true);
      });

      it('passing NewMedia should disable mediaId FormControl', () => {
        const formGroup = service.createMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.mediaId.disabled).toBe(true);

        service.resetForm(formGroup, { mediaId: null });

        expect(formGroup.controls.mediaId.disabled).toBe(true);
      });
    });
  });
});
