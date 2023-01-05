import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InvoiceLineItemService } from '../service/invoice-line-item.service';

import { InvoiceLineItemComponent } from './invoice-line-item.component';

describe('InvoiceLineItem Management Component', () => {
  let comp: InvoiceLineItemComponent;
  let fixture: ComponentFixture<InvoiceLineItemComponent>;
  let service: InvoiceLineItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'invoice-line-item', component: InvoiceLineItemComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [InvoiceLineItemComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'invoiceLineItemId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'invoiceLineItemId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(InvoiceLineItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceLineItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InvoiceLineItemService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ invoiceLineItemId: 123 }],
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
    expect(comp.invoiceLineItems?.[0]).toEqual(expect.objectContaining({ invoiceLineItemId: 123 }));
  });

  describe('trackInvoiceLineItemId', () => {
    it('Should forward to invoiceLineItemService', () => {
      const entity = { invoiceLineItemId: 123 };
      jest.spyOn(service, 'getInvoiceLineItemIdentifier');
      const invoiceLineItemId = comp.trackInvoiceLineItemId(0, entity);
      expect(service.getInvoiceLineItemIdentifier).toHaveBeenCalledWith(entity);
      expect(invoiceLineItemId).toBe(entity.invoiceLineItemId);
    });
  });
});
