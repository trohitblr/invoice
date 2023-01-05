import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceLineItemDetailComponent } from './invoice-line-item-detail.component';

describe('InvoiceLineItem Management Detail Component', () => {
  let comp: InvoiceLineItemDetailComponent;
  let fixture: ComponentFixture<InvoiceLineItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvoiceLineItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ invoiceLineItem: { invoiceLineItemId: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvoiceLineItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvoiceLineItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load invoiceLineItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.invoiceLineItem).toEqual(expect.objectContaining({ invoiceLineItemId: 123 }));
    });
  });
});
