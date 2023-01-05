import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../catalouge.test-samples';

import { CatalougeFormService } from './catalouge-form.service';

describe('Catalouge Form Service', () => {
  let service: CatalougeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CatalougeFormService);
  });

  describe('Service methods', () => {
    describe('createCatalougeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCatalougeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            catalougeId: expect.any(Object),
            name: expect.any(Object),
            cost: expect.any(Object),
            status: expect.any(Object),
            description: expect.any(Object),
            hsnNo: expect.any(Object),
            quantity: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            categorie: expect.any(Object),
            taxSlaves: expect.any(Object),
          })
        );
      });

      it('passing ICatalouge should create a new form with FormGroup', () => {
        const formGroup = service.createCatalougeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            catalougeId: expect.any(Object),
            name: expect.any(Object),
            cost: expect.any(Object),
            status: expect.any(Object),
            description: expect.any(Object),
            hsnNo: expect.any(Object),
            quantity: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            categorie: expect.any(Object),
            taxSlaves: expect.any(Object),
          })
        );
      });
    });

    describe('getCatalouge', () => {
      it('should return NewCatalouge for default Catalouge initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCatalougeFormGroup(sampleWithNewData);

        const catalouge = service.getCatalouge(formGroup) as any;

        expect(catalouge).toMatchObject(sampleWithNewData);
      });

      it('should return NewCatalouge for empty Catalouge initial value', () => {
        const formGroup = service.createCatalougeFormGroup();

        const catalouge = service.getCatalouge(formGroup) as any;

        expect(catalouge).toMatchObject({});
      });

      it('should return ICatalouge', () => {
        const formGroup = service.createCatalougeFormGroup(sampleWithRequiredData);

        const catalouge = service.getCatalouge(formGroup) as any;

        expect(catalouge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICatalouge should not enable catalougeId FormControl', () => {
        const formGroup = service.createCatalougeFormGroup();
        expect(formGroup.controls.catalougeId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.catalougeId.disabled).toBe(true);
      });

      it('passing NewCatalouge should disable catalougeId FormControl', () => {
        const formGroup = service.createCatalougeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.catalougeId.disabled).toBe(true);

        service.resetForm(formGroup, { catalougeId: null });

        expect(formGroup.controls.catalougeId.disabled).toBe(true);
      });
    });
  });
});
