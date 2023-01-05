import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IGstSlave } from '../gst-slave.model';
import { GstSlaveService } from '../service/gst-slave.service';

import { GstSlaveRoutingResolveService } from './gst-slave-routing-resolve.service';

describe('GstSlave routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GstSlaveRoutingResolveService;
  let service: GstSlaveService;
  let resultGstSlave: IGstSlave | null | undefined;

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
    routingResolveService = TestBed.inject(GstSlaveRoutingResolveService);
    service = TestBed.inject(GstSlaveService);
    resultGstSlave = undefined;
  });

  describe('resolve', () => {
    it('should return IGstSlave returned by find', () => {
      // GIVEN
      service.find = jest.fn(gstSlaveId => of(new HttpResponse({ body: { gstSlaveId } })));
      mockActivatedRouteSnapshot.params = { gstSlaveId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGstSlave = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGstSlave).toEqual({ gstSlaveId: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGstSlave = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGstSlave).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IGstSlave>({ body: null })));
      mockActivatedRouteSnapshot.params = { gstSlaveId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGstSlave = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGstSlave).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
