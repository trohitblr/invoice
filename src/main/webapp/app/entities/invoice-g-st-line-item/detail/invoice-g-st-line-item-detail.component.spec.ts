import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceGStLineItemDetailComponent } from './invoice-g-st-line-item-detail.component';

describe('InvoiceGStLineItem Management Detail Component', () => {
  let comp: InvoiceGStLineItemDetailComponent;
  let fixture: ComponentFixture<InvoiceGStLineItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvoiceGStLineItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ invoiceGStLineItem: { invoiceGStLineItemId: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvoiceGStLineItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvoiceGStLineItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load invoiceGStLineItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.invoiceGStLineItem).toEqual(expect.objectContaining({ invoiceGStLineItemId: 123 }));
    });
  });
});
