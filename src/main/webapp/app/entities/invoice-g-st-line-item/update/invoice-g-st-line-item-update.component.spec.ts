import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceGStLineItemFormService } from './invoice-g-st-line-item-form.service';
import { InvoiceGStLineItemService } from '../service/invoice-g-st-line-item.service';
import { IInvoiceGStLineItem } from '../invoice-g-st-line-item.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';

import { InvoiceGStLineItemUpdateComponent } from './invoice-g-st-line-item-update.component';

describe('InvoiceGStLineItem Management Update Component', () => {
  let comp: InvoiceGStLineItemUpdateComponent;
  let fixture: ComponentFixture<InvoiceGStLineItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceGStLineItemFormService: InvoiceGStLineItemFormService;
  let invoiceGStLineItemService: InvoiceGStLineItemService;
  let invoiceService: InvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvoiceGStLineItemUpdateComponent],
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
      .overrideTemplate(InvoiceGStLineItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceGStLineItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceGStLineItemFormService = TestBed.inject(InvoiceGStLineItemFormService);
    invoiceGStLineItemService = TestBed.inject(InvoiceGStLineItemService);
    invoiceService = TestBed.inject(InvoiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Invoice query and add missing value', () => {
      const invoiceGStLineItem: IInvoiceGStLineItem = { invoiceGStLineItemId: 456 };
      const invoices: IInvoice = { invoiceId: 33884 };
      invoiceGStLineItem.invoices = invoices;

      const invoiceCollection: IInvoice[] = [{ invoiceId: 19995 }];
      jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoices];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceGStLineItem });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(expect.objectContaining)
      );
      expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoiceGStLineItem: IInvoiceGStLineItem = { invoiceGStLineItemId: 456 };
      const invoices: IInvoice = { invoiceId: 51175 };
      invoiceGStLineItem.invoices = invoices;

      activatedRoute.data = of({ invoiceGStLineItem });
      comp.ngOnInit();

      expect(comp.invoicesSharedCollection).toContain(invoices);
      expect(comp.invoiceGStLineItem).toEqual(invoiceGStLineItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceGStLineItem>>();
      const invoiceGStLineItem = { invoiceGStLineItemId: 123 };
      jest.spyOn(invoiceGStLineItemFormService, 'getInvoiceGStLineItem').mockReturnValue(invoiceGStLineItem);
      jest.spyOn(invoiceGStLineItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceGStLineItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceGStLineItem }));
      saveSubject.complete();

      // THEN
      expect(invoiceGStLineItemFormService.getInvoiceGStLineItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceGStLineItemService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceGStLineItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceGStLineItem>>();
      const invoiceGStLineItem = { invoiceGStLineItemId: 123 };
      jest.spyOn(invoiceGStLineItemFormService, 'getInvoiceGStLineItem').mockReturnValue({ invoiceGStLineItemId: null });
      jest.spyOn(invoiceGStLineItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceGStLineItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceGStLineItem }));
      saveSubject.complete();

      // THEN
      expect(invoiceGStLineItemFormService.getInvoiceGStLineItem).toHaveBeenCalled();
      expect(invoiceGStLineItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceGStLineItem>>();
      const invoiceGStLineItem = { invoiceGStLineItemId: 123 };
      jest.spyOn(invoiceGStLineItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceGStLineItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceGStLineItemService.update).toHaveBeenCalled();
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
