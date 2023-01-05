import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RetailerService } from '../service/retailer.service';

import { RetailerComponent } from './retailer.component';

describe('Retailer Management Component', () => {
  let comp: RetailerComponent;
  let fixture: ComponentFixture<RetailerComponent>;
  let service: RetailerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'retailer', component: RetailerComponent }]), HttpClientTestingModule],
      declarations: [RetailerComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'retailerId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'retailerId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RetailerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RetailerService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ retailerId: 123 }],
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
    expect(comp.retailers?.[0]).toEqual(expect.objectContaining({ retailerId: 123 }));
  });

  describe('trackRetailerId', () => {
    it('Should forward to retailerService', () => {
      const entity = { retailerId: 123 };
      jest.spyOn(service, 'getRetailerIdentifier');
      const retailerId = comp.trackRetailerId(0, entity);
      expect(service.getRetailerIdentifier).toHaveBeenCalledWith(entity);
      expect(retailerId).toBe(entity.retailerId);
    });
  });
});
