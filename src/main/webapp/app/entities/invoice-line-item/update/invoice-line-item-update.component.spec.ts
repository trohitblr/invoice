import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceLineItemFormService } from './invoice-line-item-form.service';
import { InvoiceLineItemService } from '../service/invoice-line-item.service';
import { IInvoiceLineItem } from '../invoice-line-item.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';

import { InvoiceLineItemUpdateComponent } from './invoice-line-item-update.component';

describe('InvoiceLineItem Management Update Component', () => {
  let comp: InvoiceLineItemUpdateComponent;
  let fixture: ComponentFixture<InvoiceLineItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceLineItemFormService: InvoiceLineItemFormService;
  let invoiceLineItemService: InvoiceLineItemService;
  let invoiceService: InvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvoiceLineItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvoiceLineItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceLineItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceLineItemFormService = TestBed.inject(InvoiceLineItemFormService);
    invoiceLineItemService = TestBed.inject(InvoiceLineItemService);
    invoiceService = TestBed.inject(InvoiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Invoice query and add missing value', () => {
      const invoiceLineItem: IInvoiceLineItem = { invoiceLineItemId: 456 };
      const invoices: IInvoice = { invoiceId: 25902 };
      invoiceLineItem.invoices = invoices;

      const invoiceCollection: IInvoice[] = [{ invoiceId: 37232 }];
      jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoices];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceLineItem });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(expect.objectContaining)
      );
      expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoiceLineItem: IInvoiceLineItem = { invoiceLineItemId: 456 };
      const invoices: IInvoice = { invoiceId: 61917 };
      invoiceLineItem.invoices = invoices;

      activatedRoute.data = of({ invoiceLineItem });
      comp.ngOnInit();

      expect(comp.invoicesSharedCollection).toContain(invoices);
      expect(comp.invoiceLineItem).toEqual(invoiceLineItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLineItem>>();
      const invoiceLineItem = { invoiceLineItemId: 123 };
      jest.spyOn(invoiceLineItemFormService, 'getInvoiceLineItem').mockReturnValue(invoiceLineItem);
      jest.spyOn(invoiceLineItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLineItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceLineItem }));
      saveSubject.complete();

      // THEN
      expect(invoiceLineItemFormService.getInvoiceLineItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceLineItemService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceLineItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLineItem>>();
      const invoiceLineItem = { invoiceLineItemId: 123 };
      jest.spyOn(invoiceLineItemFormService, 'getInvoiceLineItem').mockReturnValue({ invoiceLineItemId: null });
      jest.spyOn(invoiceLineItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLineItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceLineItem }));
      saveSubject.complete();

      // THEN
      expect(invoiceLineItemFormService.getInvoiceLineItem).toHaveBeenCalled();
      expect(invoiceLineItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLineItem>>();
      const invoiceLineItem = { invoiceLineItemId: 123 };
      jest.spyOn(invoiceLineItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLineItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceLineItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoice', () => {
      it('Should forward to invoiceService', () => {
        const entity = { invoiceId: 123 };
        const entity2 = { invoiceId: 456 };
        jest.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
