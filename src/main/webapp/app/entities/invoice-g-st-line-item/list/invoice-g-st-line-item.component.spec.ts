import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InvoiceGStLineItemService } from '../service/invoice-g-st-line-item.service';

import { InvoiceGStLineItemComponent } from './invoice-g-st-line-item.component';

describe('InvoiceGStLineItem Management Component', () => {
  let comp: InvoiceGStLineItemComponent;
  let fixture: ComponentFixture<InvoiceGStLineItemComponent>;
  let service: InvoiceGStLineItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'invoice-g-st-line-item', component: InvoiceGStLineItemComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [InvoiceGStLineItemComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'invoiceGStLineItemId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'invoiceGStLineItemId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(InvoiceGStLineItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceGStLineItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InvoiceGStLineItemService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ invoiceGStLineItemId: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.invoiceGStLineItems?.[0]).toEqual(expect.objectContaining({ invoiceGStLineItemId: 123 }));
  });

  describe('trackInvoiceGStLineItemId', () => {
    it('Should forward to invoiceGStLineItemService', () => {
      const entity = { invoiceGStLineItemId: 123 };
      jest.spyOn(service, 'getInvoiceGStLineItemIdentifier');
      const invoiceGStLineItemId = comp.trackInvoiceGStLineItemId(0, entity);
      expect(service.getInvoiceGStLineItemIdentifier).toHaveBeenCalledWith(entity);
      expect(invoiceGStLineItemId).toBe(entity.invoiceGStLineItemId);
    });
  });
});
