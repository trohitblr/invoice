import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRetailer } from '../retailer.model';
import { RetailerService } from '../service/retailer.service';

import { RetailerRoutingResolveService } from './retailer-routing-resolve.service';

describe('Retailer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RetailerRoutingResolveService;
  let service: RetailerService;
  let resultRetailer: IRetailer | null | undefined;

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
    routingResolveService = TestBed.inject(RetailerRoutingResolveService);
    service = TestBed.inject(RetailerService);
    resultRetailer = undefined;
  });

  describe('resolve', () => {
    it('should return IRetailer returned by find', () => {
      // GIVEN
      service.find = jest.fn(retailerId => of(new HttpResponse({ body: { retailerId } })));
      mockActivatedRouteSnapshot.params = { retailerId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRetailer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRetailer).toEqual({ retailerId: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRetailer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRetailer).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IRetailer>({ body: null })));
      mockActivatedRouteSnapshot.params = { retailerId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRetailer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRetailer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
