import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMedia } from '../media.model';
import { MediaService } from '../service/media.service';

import { MediaRoutingResolveService } from './media-routing-resolve.service';

describe('Media routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MediaRoutingResolveService;
  let service: MediaService;
  let resultMedia: IMedia | null | undefined;

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
    routingResolveService = TestBed.inject(MediaRoutingResolveService);
    service = TestBed.inject(MediaService);
    resultMedia = undefined;
  });

  describe('resolve', () => {
    it('should return IMedia returned by find', () => {
      // GIVEN
      service.find = jest.fn(mediaId => of(new HttpResponse({ body: { mediaId } })));
      mockActivatedRouteSnapshot.params = { mediaId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMedia = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMedia).toEqual({ mediaId: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMedia = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMedia).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IMedia>({ body: null })));
      mockActivatedRouteSnapshot.params = { mediaId: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMedia = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMedia).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
