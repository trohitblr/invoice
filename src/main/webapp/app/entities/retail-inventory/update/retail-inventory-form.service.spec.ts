import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../retail-inventory.test-samples';

import { RetailInventoryFormService } from './retail-inventory-form.service';

describe('RetailInventory Form Service', () => {
  let service: RetailInventoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RetailInventoryFormService);
  });

  describe('Service methods', () => {
    describe('createRetailInventoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRetailInventoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            retailInventoryId: expect.any(Object),
            quantity: expect.any(Object),
            availableQty: expect.any(Object),
            soldQty: expect.any(Object),
            maxLimit: expect.any(Object),
            minLimit: expect.any(Object),
            status: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            catalougs: expect.any(Object),
          })
        );
      });

      it('passing IRetailInventory should create a new form with FormGroup', () => {
        const formGroup = service.createRetailInventoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            retailInventoryId: expect.any(Object),
            quantity: expect.any(Object),
            availableQty: expect.any(Object),
            soldQty: expect.any(Object),
            maxLimit: expect.any(Object),
            minLimit: expect.any(Object),
            status: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            catalougs: expect.any(Object),
          })
        );
      });
    });

    describe('getRetailInventory', () => {
      it('should return NewRetailInventory for default RetailInventory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRetailInventoryFormGroup(sampleWithNewData);

        const retailInventory = service.getRetailInventory(formGroup) as any;

        expect(retailInventory).toMatchObject(sampleWithNewData);
      });

      it('should return NewRetailInventory for empty RetailInventory initial value', () => {
        const formGroup = service.createRetailInventoryFormGroup();

        const retailInventory = service.getRetailInventory(formGroup) as any;

        expect(retailInventory).toMatchObject({});
      });

      it('should return IRetailInventory', () => {
        const formGroup = service.createRetailInventoryFormGroup(sampleWithRequiredData);

        const retailInventory = service.getRetailInventory(formGroup) as any;

        expect(retailInventory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRetailInventory should not enable retailInventoryId FormControl', () => {
        const formGroup = service.createRetailInventoryFormGroup();
        expect(formGroup.controls.retailInventoryId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.retailInventoryId.disabled).toBe(true);
      });

      it('passing NewRetailInventory should disable retailInventoryId FormControl', () => {
        const formGroup = service.createRetailInventoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.retailInventoryId.disabled).toBe(true);

        service.resetForm(formGroup, { retailInventoryId: null });

        expect(formGroup.controls.retailInventoryId.disabled).toBe(true);
      });
    });
  });
});
