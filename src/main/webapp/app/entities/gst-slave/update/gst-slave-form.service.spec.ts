import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../gst-slave.test-samples';

import { GstSlaveFormService } from './gst-slave-form.service';

describe('GstSlave Form Service', () => {
  let service: GstSlaveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GstSlaveFormService);
  });

  describe('Service methods', () => {
    describe('createGstSlaveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGstSlaveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            gstSlaveId: expect.any(Object),
            tax: expect.any(Object),
            taxPercentage: expect.any(Object),
          })
        );
      });

      it('passing IGstSlave should create a new form with FormGroup', () => {
        const formGroup = service.createGstSlaveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            gstSlaveId: expect.any(Object),
            tax: expect.any(Object),
            taxPercentage: expect.any(Object),
          })
        );
      });
    });

    describe('getGstSlave', () => {
      it('should return NewGstSlave for default GstSlave initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGstSlaveFormGroup(sampleWithNewData);

        const gstSlave = service.getGstSlave(formGroup) as any;

        expect(gstSlave).toMatchObject(sampleWithNewData);
      });

      it('should return NewGstSlave for empty GstSlave initial value', () => {
        const formGroup = service.createGstSlaveFormGroup();

        const gstSlave = service.getGstSlave(formGroup) as any;

        expect(gstSlave).toMatchObject({});
      });

      it('should return IGstSlave', () => {
        const formGroup = service.createGstSlaveFormGroup(sampleWithRequiredData);

        const gstSlave = service.getGstSlave(formGroup) as any;

        expect(gstSlave).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGstSlave should not enable gstSlaveId FormControl', () => {
        const formGroup = service.createGstSlaveFormGroup();
        expect(formGroup.controls.gstSlaveId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.gstSlaveId.disabled).toBe(true);
      });

      it('passing NewGstSlave should disable gstSlaveId FormControl', () => {
        const formGroup = service.createGstSlaveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.gstSlaveId.disabled).toBe(true);

        service.resetForm(formGroup, { gstSlaveId: null });

        expect(formGroup.controls.gstSlaveId.disabled).toBe(true);
      });
    });
  });
});
