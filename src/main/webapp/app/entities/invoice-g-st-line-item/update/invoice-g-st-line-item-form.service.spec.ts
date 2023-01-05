import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../invoice-g-st-line-item.test-samples';

import { InvoiceGStLineItemFormService } from './invoice-g-st-line-item-form.service';

describe('InvoiceGStLineItem Form Service', () => {
  let service: InvoiceGStLineItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceGStLineItemFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceGStLineItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceGStLineItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            invoiceGStLineItemId: expect.any(Object),
            articleType: expect.any(Object),
            articleId: expect.any(Object),
            hsnsac: expect.any(Object),
            sgst: expect.any(Object),
            cgst: expect.any(Object),
            sgstTaxAmount: expect.any(Object),
            cgstTaxAmount: expect.any(Object),
            amount: expect.any(Object),
            discount: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            invoices: expect.any(Object),
          })
        );
      });

      it('passing IInvoiceGStLineItem should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceGStLineItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            invoiceGStLineItemId: expect.any(Object),
            articleType: expect.any(Object),
            articleId: expect.any(Object),
            hsnsac: expect.any(Object),
            sgst: expect.any(Object),
            cgst: expect.any(Object),
            sgstTaxAmount: expect.any(Object),
            cgstTaxAmount: expect.any(Object),
            amount: expect.any(Object),
            discount: expect.any(Object),
            createdOn: expect.any(Object),
            updatedOn: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            invoices: expect.any(Object),
          })
        );
      });
    });

    describe('getInvoiceGStLineItem', () => {
      it('should return NewInvoiceGStLineItem for default InvoiceGStLineItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInvoiceGStLineItemFormGroup(sampleWithNewData);

        const invoiceGStLineItem = service.getInvoiceGStLineItem(formGroup) as any;

        expect(invoiceGStLineItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceGStLineItem for empty InvoiceGStLineItem initial value', () => {
        const formGroup = service.createInvoiceGStLineItemFormGroup();

        const invoiceGStLineItem = service.getInvoiceGStLineItem(formGroup) as any;

        expect(invoiceGStLineItem).toMatchObject({});
      });

      it('should return IInvoiceGStLineItem', () => {
        const formGroup = service.createInvoiceGStLineItemFormGroup(sampleWithRequiredData);

        const invoiceGStLineItem = service.getInvoiceGStLineItem(formGroup) as any;

        expect(invoiceGStLineItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceGStLineItem should not enable invoiceGStLineItemId FormControl', () => {
        const formGroup = service.createInvoiceGStLineItemFormGroup();
        expect(formGroup.controls.invoiceGStLineItemId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.invoiceGStLineItemId.disabled).toBe(true);
      });

      it('passing NewInvoiceGStLineItem should disable invoiceGStLineItemId FormControl', () => {
        const formGroup = service.createInvoiceGStLineItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.invoiceGStLineItemId.disabled).toBe(true);

        service.resetForm(formGroup, { invoiceGStLineItemId: null });

        expect(formGroup.controls.invoiceGStLineItemId.disabled).toBe(true);
      });
    });
  });
});
