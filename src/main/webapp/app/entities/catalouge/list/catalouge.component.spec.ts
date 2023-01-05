import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CatalougeService } from '../service/catalouge.service';

import { CatalougeComponent } from './catalouge.component';

describe('Catalouge Management Component', () => {
  let comp: CatalougeComponent;
  let fixture: ComponentFixture<CatalougeComponent>;
  let service: CatalougeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'catalouge', component: CatalougeComponent }]), HttpClientTestingModule],
      declarations: [CatalougeComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'catalougeId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'catalougeId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CatalougeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalougeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CatalougeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ catalougeId: 123 }],
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
    expect(comp.catalouges?.[0]).toEqual(expect.objectContaining({ catalougeId: 123 }));
  });

  describe('trackCatalougeId', () => {
    it('Should forward to catalougeService', () => {
      const entity = { catalougeId: 123 };
      jest.spyOn(service, 'getCatalougeIdentifier');
      const catalougeId = comp.trackCatalougeId(0, entity);
      expect(service.getCatalougeIdentifier).toHaveBeenCalledWith(entity);
      expect(catalougeId).toBe(entity.catalougeId);
    });
  });
});
