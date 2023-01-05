import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../retailer.test-samples';

import { RetailerFormService } from './retailer-form.service';

describe('Retailer Form Service', () => {
  let service: RetailerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RetailerFormService);
  });

  describe('Service methods', () => {
    describe('createRetailerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRetailerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            retailerId: expect.any(Object),
            name: expect.any(Object),
            owner: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            gstNumber: expect.any(Object),
            status: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            invoiceAddress: expect.any(Object),
            billingAddress: expect.any(Object),
            logo: expect.any(Object),
            customers: expect.any(Object),
          })
        );
      });

      it('passing IRetailer should create a new form with FormGroup', () => {
        const formGroup = service.createRetailerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            retailerId: expect.any(Object),
            name: expect.any(Object),
            owner: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            gstNumber: expect.any(Object),
            status: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            invoiceAddress: expect.any(Object),
            billingAddress: expect.any(Object),
            logo: expect.any(Object),
            customers: expect.any(Object),
          })
        );
      });
    });

    describe('getRetailer', () => {
      it('should return NewRetailer for default Retailer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRetailerFormGroup(sampleWithNewData);

        const retailer = service.getRetailer(formGroup) as any;

        expect(retailer).toMatchObject(sampleWithNewData);
      });

      it('should return NewRetailer for empty Retailer initial value', () => {
        const formGroup = service.createRetailerFormGroup();

        const retailer = service.getRetailer(formGroup) as any;

        expect(retailer).toMatchObject({});
      });

      it('should return IRetailer', () => {
        const formGroup = service.createRetailerFormGroup(sampleWithRequiredData);

        const retailer = service.getRetailer(formGroup) as any;

        expect(retailer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRetailer should not enable retailerId FormControl', () => {
        const formGroup = service.createRetailerFormGroup();
        expect(formGroup.controls.retailerId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.retailerId.disabled).toBe(true);
      });

      it('passing NewRetailer should disable retailerId FormControl', () => {
        const formGroup = service.createRetailerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.retailerId.disabled).toBe(true);

        service.resetForm(formGroup, { retailerId: null });

        expect(formGroup.controls.retailerId.disabled).toBe(true);
      });
    });
  });
});
