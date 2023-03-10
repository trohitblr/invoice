import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer.test-samples';

import { CustomerFormService } from './customer-form.service';

describe('Customer Form Service', () => {
  let service: CustomerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            customerId: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            invoiceAddress: expect.any(Object),
            billingAddress: expect.any(Object),
            retailers: expect.any(Object),
          })
        );
      });

      it('passing ICustomer should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            customerId: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            invoiceAddress: expect.any(Object),
            billingAddress: expect.any(Object),
            retailers: expect.any(Object),
          })
        );
      });
    });

    describe('getCustomer', () => {
      it('should return NewCustomer for default Customer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCustomerFormGroup(sampleWithNewData);

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomer for empty Customer initial value', () => {
        const formGroup = service.createCustomerFormGroup();

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject({});
      });

      it('should return ICustomer', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomer should not enable customerId FormControl', () => {
        const formGroup = service.createCustomerFormGroup();
        expect(formGroup.controls.customerId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.customerId.disabled).toBe(true);
      });

      it('passing NewCustomer should disable customerId FormControl', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.customerId.disabled).toBe(true);

        service.resetForm(formGroup, { customerId: null });

        expect(formGroup.controls.customerId.disabled).toBe(true);
      });
    });
  });
});
