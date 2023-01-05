import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../retailer-user.test-samples';

import { RetailerUserFormService } from './retailer-user-form.service';

describe('RetailerUser Form Service', () => {
  let service: RetailerUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RetailerUserFormService);
  });

  describe('Service methods', () => {
    describe('createRetailerUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRetailerUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            retailerUserId: expect.any(Object),
            userId: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            type: expect.any(Object),
            encPassword: expect.any(Object),
            status: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            retailers: expect.any(Object),
          })
        );
      });

      it('passing IRetailerUser should create a new form with FormGroup', () => {
        const formGroup = service.createRetailerUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            retailerUserId: expect.any(Object),
            userId: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            type: expect.any(Object),
            encPassword: expect.any(Object),
            status: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            retailers: expect.any(Object),
          })
        );
      });
    });

    describe('getRetailerUser', () => {
      it('should return NewRetailerUser for default RetailerUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRetailerUserFormGroup(sampleWithNewData);

        const retailerUser = service.getRetailerUser(formGroup) as any;

        expect(retailerUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewRetailerUser for empty RetailerUser initial value', () => {
        const formGroup = service.createRetailerUserFormGroup();

        const retailerUser = service.getRetailerUser(formGroup) as any;

        expect(retailerUser).toMatchObject({});
      });

      it('should return IRetailerUser', () => {
        const formGroup = service.createRetailerUserFormGroup(sampleWithRequiredData);

        const retailerUser = service.getRetailerUser(formGroup) as any;

        expect(retailerUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRetailerUser should not enable retailerUserId FormControl', () => {
        const formGroup = service.createRetailerUserFormGroup();
        expect(formGroup.controls.retailerUserId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.retailerUserId.disabled).toBe(true);
      });

      it('passing NewRetailerUser should disable retailerUserId FormControl', () => {
        const formGroup = service.createRetailerUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.retailerUserId.disabled).toBe(true);

        service.resetForm(formGroup, { retailerUserId: null });

        expect(formGroup.controls.retailerUserId.disabled).toBe(true);
      });
    });
  });
});
