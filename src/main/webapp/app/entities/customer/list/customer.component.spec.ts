import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CustomerService } from '../service/customer.service';

import { CustomerComponent } from './customer.component';

describe('Customer Management Component', () => {
  let comp: CustomerComponent;
  let fixture: ComponentFixture<CustomerComponent>;
  let service: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'customer', component: CustomerComponent }]), HttpClientTestingModule],
      declarations: [CustomerComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'customerId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'customerId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CustomerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CustomerService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ customerId: 123 }],
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
    expect(comp.customers?.[0]).toEqual(expect.objectContaining({ customerId: 123 }));
  });

  describe('trackCustomerId', () => {
    it('Should forward to customerService', () => {
      const entity = { customerId: 123 };
      jest.spyOn(service, 'getCustomerIdentifier');
      const customerId = comp.trackCustomerId(0, entity);
      expect(service.getCustomerIdentifier).toHaveBeenCalledWith(entity);
      expect(customerId).toBe(entity.customerId);
    });
  });
});
