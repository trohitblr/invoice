import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CatalougeFormService } from './catalouge-form.service';
import { CatalougeService } from '../service/catalouge.service';
import { ICatalouge } from '../catalouge.model';
import { ICategories } from 'app/entities/categories/categories.model';
import { CategoriesService } from 'app/entities/categories/service/categories.service';
import { IGstSlave } from 'app/entities/gst-slave/gst-slave.model';
import { GstSlaveService } from 'app/entities/gst-slave/service/gst-slave.service';

import { CatalougeUpdateComponent } from './catalouge-update.component';

describe('Catalouge Management Update Component', () => {
  let comp: CatalougeUpdateComponent;
  let fixture: ComponentFixture<CatalougeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let catalougeFormService: CatalougeFormService;
  let catalougeService: CatalougeService;
  let categoriesService: CategoriesService;
  let gstSlaveService: GstSlaveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CatalougeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CatalougeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalougeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalougeFormService = TestBed.inject(CatalougeFormService);
    catalougeService = TestBed.inject(CatalougeService);
    categoriesService = TestBed.inject(CategoriesService);
    gstSlaveService = TestBed.inject(GstSlaveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categories query and add missing value', () => {
      const catalouge: ICatalouge = { catalougeId: 456 };
      const categorie: ICategories = { categoryId: 21824 };
      catalouge.categorie = categorie;

      const categoriesCollection: ICategories[] = [{ categoryId: 50852 }];
      jest.spyOn(categoriesService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriesCollection })));
      const additionalCategories = [categorie];
      const expectedCollection: ICategories[] = [...additionalCategories, ...categoriesCollection];
      jest.spyOn(categoriesService, 'addCategoriesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalouge });
      comp.ngOnInit();

      expect(categoriesService.query).toHaveBeenCalled();
      expect(categoriesService.addCategoriesToCollectionIfMissing).toHaveBeenCalledWith(
        categoriesCollection,
        ...additionalCategories.map(expect.objectContaining)
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call GstSlave query and add missing value', () => {
      const catalouge: ICatalouge = { catalougeId: 456 };
      const taxSlaves: IGstSlave = { gstSlaveId: 60128 };
      catalouge.taxSlaves = taxSlaves;

      const gstSlaveCollection: IGstSlave[] = [{ gstSlaveId: 89229 }];
      jest.spyOn(gstSlaveService, 'query').mockReturnValue(of(new HttpResponse({ body: gstSlaveCollection })));
      const additionalGstSlaves = [taxSlaves];
      const expectedCollection: IGstSlave[] = [...additionalGstSlaves, ...gstSlaveCollection];
      jest.spyOn(gstSlaveService, 'addGstSlaveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalouge });
      comp.ngOnInit();

      expect(gstSlaveService.query).toHaveBeenCalled();
      expect(gstSlaveService.addGstSlaveToCollectionIfMissing).toHaveBeenCalledWith(
        gstSlaveCollection,
        ...additionalGstSlaves.map(expect.objectContaining)
      );
      expect(comp.gstSlavesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const catalouge: ICatalouge = { catalougeId: 456 };
      const categorie: ICategories = { categoryId: 92596 };
      catalouge.categorie = categorie;
      const taxSlaves: IGstSlave = { gstSlaveId: 63011 };
      catalouge.taxSlaves = taxSlaves;

      activatedRoute.data = of({ catalouge });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(categorie);
      expect(comp.gstSlavesSharedCollection).toContain(taxSlaves);
      expect(comp.catalouge).toEqual(catalouge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalouge>>();
      const catalouge = { catalougeId: 123 };
      jest.spyOn(catalougeFormService, 'getCatalouge').mockReturnValue(catalouge);
      jest.spyOn(catalougeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalouge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalouge }));
      saveSubject.complete();

      // THEN
      expect(catalougeFormService.getCatalouge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalougeService.update).toHaveBeenCalledWith(expect.objectContaining(catalouge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalouge>>();
      const catalouge = { catalougeId: 123 };
      jest.spyOn(catalougeFormService, 'getCatalouge').mockReturnValue({ catalougeId: null });
      jest.spyOn(catalougeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalouge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalouge }));
      saveSubject.complete();

      // THEN
      expect(catalougeFormService.getCatalouge).toHaveBeenCalled();
      expect(catalougeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalouge>>();
      const catalouge = { catalougeId: 123 };
      jest.spyOn(catalougeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalouge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalougeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategories', () => {
      it('Should forward to categoriesService', () => {
        const entity = { categoryId: 123 };
        const entity2 = { categoryId: 456 };
        jest.spyOn(categoriesService, 'compareCategories');
        comp.compareCategories(entity, entity2);
        expect(categoriesService.compareCategories).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGstSlave', () => {
      it('Should forward to gstSlaveService', () => {
        const entity = { gstSlaveId: 123 };
        const entity2 = { gstSlaveId: 456 };
        jest.spyOn(gstSlaveService, 'compareGstSlave');
        comp.compareGstSlave(entity, entity2);
        expect(gstSlaveService.compareGstSlave).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
