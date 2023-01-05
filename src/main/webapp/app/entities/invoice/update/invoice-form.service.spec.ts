import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../invoice.test-samples';

import { InvoiceFormService } from './invoice-form.service';

describe('Invoice Form Service', () => {
  let service: InvoiceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            invoiceId: expect.any(Object),
            billNo: expect.any(Object),
            taxPercentage: expect.any(Object),
            paymentType: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            retailers: expect.any(Object),
          })
        );
      });

      it('passing IInvoice should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            invoiceId: expect.any(Object),
            billNo: expect.any(Object),
            taxPercentage: expect.any(Object),
            paymentType: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            retailers: expect.any(Object),
          })
        );
      });
    });

    describe('getInvoice', () => {
      it('should return NewInvoice for default Invoice initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInvoiceFormGroup(sampleWithNewData);

        const invoice = service.getInvoice(formGroup) as any;

        expect(invoice).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoice for empty Invoice initial value', () => {
        const formGroup = service.createInvoiceFormGroup();

        const invoice = service.getInvoice(formGroup) as any;

        expect(invoice).toMatchObject({});
      });

      it('should return IInvoice', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);

        const invoice = service.getInvoice(formGroup) as any;

        expect(invoice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoice should not enable invoiceId FormControl', () => {
        const formGroup = service.createInvoiceFormGroup();
        expect(formGroup.controls.invoiceId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.invoiceId.disabled).toBe(true);
      });

      it('passing NewInvoice should disable invoiceId FormControl', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.invoiceId.disabled).toBe(true);

        service.resetForm(formGroup, { invoiceId: null });

        expect(formGroup.controls.invoiceId.disabled).toBe(true);
      });
    });
  });
});
