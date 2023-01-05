import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICatalouge } from '../catalouge.model';
import { CatalougeService } from '../service/catalouge.service';

import { CatalougeRoutingResolveService } from './catalouge-routing-resolve.service';

describe('Catalouge routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CatalougeRoutingResolveService;
  let service: CatalougeService;
  let resultCatalouge: ICatalouge | null | undefined;

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
    routingResolveService = TestBed.inject(CatalougeRoutingResolveService);
    service = TestBed.inject(CatalougeService);
    resultCatalouge = undefined;
  });

  describe('resolve', () => {
    it('should return ICatalouge returned by find', () => {
      // GIVEN
      service.find = jest.fn(catalougeId => of(new HttpResponse({ body: { catalougeId } })));
      mockActivatedRouteSnapshot.params = { catalougeId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalouge = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalouge).toEqual({ catalougeId: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalouge = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCatalouge).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICatalouge>({ body: null })));
      mockActivatedRouteSnapshot.params = { catalougeId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCatalouge = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCatalouge).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
