import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RetailerUserService } from '../service/retailer-user.service';

import { RetailerUserComponent } from './retailer-user.component';

describe('RetailerUser Management Component', () => {
  let comp: RetailerUserComponent;
  let fixture: ComponentFixture<RetailerUserComponent>;
  let service: RetailerUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'retailer-user', component: RetailerUserComponent }]), HttpClientTestingModule],
      declarations: [RetailerUserComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'retailerUserId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'retailerUserId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RetailerUserComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailerUserComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RetailerUserService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ retailerUserId: 123 }],
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
    expect(comp.retailerUsers?.[0]).toEqual(expect.objectContaining({ retailerUserId: 123 }));
  });

  describe('trackRetailerUserId', () => {
    it('Should forward to retailerUserService', () => {
      const entity = { retailerUserId: 123 };
      jest.spyOn(service, 'getRetailerUserIdentifier');
      const retailerUserId = comp.trackRetailerUserId(0, entity);
      expect(service.getRetailerUserIdentifier).toHaveBeenCalledWith(entity);
      expect(retailerUserId).toBe(entity.retailerUserId);
    });
  });
});
