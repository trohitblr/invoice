import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CategoriesService } from '../service/categories.service';

import { CategoriesComponent } from './categories.component';

describe('Categories Management Component', () => {
  let comp: CategoriesComponent;
  let fixture: ComponentFixture<CategoriesComponent>;
  let service: CategoriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'categories', component: CategoriesComponent }]), HttpClientTestingModule],
      declarations: [CategoriesComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'categoryId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'categoryId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CategoriesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CategoriesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ categoryId: 123 }],
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
    expect(comp.categories?.[0]).toEqual(expect.objectContaining({ categoryId: 123 }));
  });

  describe('trackCategoryId', () => {
    it('Should forward to categoriesService', () => {
      const entity = { categoryId: 123 };
      jest.spyOn(service, 'getCategoriesIdentifier');
      const categoryId = comp.trackCategoryId(0, entity);
      expect(service.getCategoriesIdentifier).toHaveBeenCalledWith(entity);
      expect(categoryId).toBe(entity.categoryId);
    });
  });
});
