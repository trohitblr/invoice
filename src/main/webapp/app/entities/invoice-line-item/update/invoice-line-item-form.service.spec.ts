import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../invoice-line-item.test-samples';

import { InvoiceLineItemFormService } from './invoice-line-item-form.service';

describe('InvoiceLineItem Form Service', () => {
  let service: InvoiceLineItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceLineItemFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceLineItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceLineItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            invoiceLineItemId: expect.any(Object),
            articleType: expect.any(Object),
            articleId: expect.any(Object),
            quantity: expect.any(Object),
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

      it('passing IInvoiceLineItem should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceLineItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            invoiceLineItemId: expect.any(Object),
            articleType: expect.any(Object),
            articleId: expect.any(Object),
            quantity: expect.any(Object),
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

    describe('getInvoiceLineItem', () => {
      it('should return NewInvoiceLineItem for default InvoiceLineItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInvoiceLineItemFormGroup(sampleWithNewData);

        const invoiceLineItem = service.getInvoiceLineItem(formGroup) as any;

        expect(invoiceLineItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceLineItem for empty InvoiceLineItem initial value', () => {
        const formGroup = service.createInvoiceLineItemFormGroup();

        const invoiceLineItem = service.getInvoiceLineItem(formGroup) as any;

        expect(invoiceLineItem).toMatchObject({});
      });

      it('should return IInvoiceLineItem', () => {
        const formGroup = service.createInvoiceLineItemFormGroup(sampleWithRequiredData);

        const invoiceLineItem = service.getInvoiceLineItem(formGroup) as any;

        expect(invoiceLineItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceLineItem should not enable invoiceLineItemId FormControl', () => {
        const formGroup = service.createInvoiceLineItemFormGroup();
        expect(formGroup.controls.invoiceLineItemId.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.invoiceLineItemId.disabled).toBe(true);
      });

      it('passing NewInvoiceLineItem should disable invoiceLineItemId FormControl', () => {
        const formGroup = service.createInvoiceLineItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.invoiceLineItemId.disabled).toBe(true);

        service.resetForm(formGroup, { invoiceLineItemId: null });

        expect(formGroup.controls.invoiceLineItemId.disabled).toBe(true);
      });
    });
  });
});
