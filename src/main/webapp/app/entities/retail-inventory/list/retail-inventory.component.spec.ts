import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RetailInventoryService } from '../service/retail-inventory.service';

import { RetailInventoryComponent } from './retail-inventory.component';

describe('RetailInventory Management Component', () => {
  let comp: RetailInventoryComponent;
  let fixture: ComponentFixture<RetailInventoryComponent>;
  let service: RetailInventoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'retail-inventory', component: RetailInventoryComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [RetailInventoryComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'retailInventoryId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'retailInventoryId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RetailInventoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailInventoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RetailInventoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ retailInventoryId: 123 }],
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
    expect(comp.retailInventories?.[0]).toEqual(expect.objectContaining({ retailInventoryId: 123 }));
  });

  describe('trackRetailInventoryId', () => {
    it('Should forward to retailInventoryService', () => {
      const entity = { retailInventoryId: 123 };
      jest.spyOn(service, 'getRetailInventoryIdentifier');
      const retailInventoryId = comp.trackRetailInventoryId(0, entity);
      expect(service.getRetailInventoryIdentifier).toHaveBeenCalledWith(entity);
      expect(retailInventoryId).toBe(entity.retailInventoryId);
    });
  });
});
