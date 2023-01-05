import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInvoiceLineItem } from '../invoice-line-item.model';
import { InvoiceLineItemService } from '../service/invoice-line-item.service';

import { InvoiceLineItemRoutingResolveService } from './invoice-line-item-routing-resolve.service';

describe('InvoiceLineItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InvoiceLineItemRoutingResolveService;
  let service: InvoiceLineItemService;
  let resultInvoiceLineItem: IInvoiceLineItem | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(InvoiceLineItemRoutingResolveService);
    service = TestBed.inject(InvoiceLineItemService);
    resultInvoiceLineItem = undefined;
  });

  describe('resolve', () => {
    it('should return IInvoiceLineItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(invoiceLineItemId => of(new HttpResponse({ body: { invoiceLineItemId } })));
      mockActivatedRouteSnapshot.params = { invoiceLineItemId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceLineItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvoiceLineItem).toEqual({ invoiceLineItemId: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceLineItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInvoiceLineItem).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IInvoiceLineItem>({ body: null })));
      mockActivatedRouteSnapshot.params = { invoiceLineItemId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceLineItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvoiceLineItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
