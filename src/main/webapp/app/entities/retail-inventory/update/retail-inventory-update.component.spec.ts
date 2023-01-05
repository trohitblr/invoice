import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RetailInventoryFormService } from './retail-inventory-form.service';
import { RetailInventoryService } from '../service/retail-inventory.service';
import { IRetailInventory } from '../retail-inventory.model';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';

import { RetailInventoryUpdateComponent } from './retail-inventory-update.component';

describe('RetailInventory Management Update Component', () => {
  let comp: RetailInventoryUpdateComponent;
  let fixture: ComponentFixture<RetailInventoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let retailInventoryFormService: RetailInventoryFormService;
  let retailInventoryService: RetailInventoryService;
  let retailerService: RetailerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RetailInventoryUpdateComponent],
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
      .overrideTemplate(RetailInventoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailInventoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    retailInventoryFormService = TestBed.inject(RetailInventoryFormService);
    retailInventoryService = TestBed.inject(RetailInventoryService);
    retailerService = TestBed.inject(RetailerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Retailer query and add missing value', () => {
      const retailInventory: IRetailInventory = { retailInventoryId: 456 };
      const catalougs: IRetailer = { retailerId: 23664 };
      retailInventory.catalougs = catalougs;

      const retailerCollection: IRetailer[] = [{ retailerId: 80949 }];
      jest.spyOn(retailerService, 'query').mockReturnValue(of(new HttpResponse({ body: retailerCollection })));
      const additionalRetailers = [catalougs];
      const expectedCollection: IRetailer[] = [...additionalRetailers, ...retailerCollection];
      jest.spyOn(retailerService, 'addRetailerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ retailInventory });
      comp.ngOnInit();

      expect(retailerService.query).toHaveBeenCalled();
      expect(retailerService.addRetailerToCollectionIfMissing).toHaveBeenCalledWith(
        retailerCollection,
        ...additionalRetailers.map(expect.objectContaining)
      );
      expect(comp.retailersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const retailInventory: IRetailInventory = { retailInventoryId: 456 };
      const catalougs: IRetailer = { retailerId: 74294 };
      retailInventory.catalougs = catalougs;

      activatedRoute.data = of({ retailInventory });
      comp.ngOnInit();

      expect(comp.retailersSharedCollection).toContain(catalougs);
      expect(comp.retailInventory).toEqual(retailInventory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailInventory>>();
      const retailInventory = { retailInventoryId: 123 };
      jest.spyOn(retailInventoryFormService, 'getRetailInventory').mockReturnValue(retailInventory);
      jest.spyOn(retailInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailInventory }));
      saveSubject.complete();

      // THEN
      expect(retailInventoryFormService.getRetailInventory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(retailInventoryService.update).toHaveBeenCalledWith(expect.objectContaining(retailInventory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailInventory>>();
      const retailInventory = { retailInventoryId: 123 };
      jest.spyOn(retailInventoryFormService, 'getRetailInventory').mockReturnValue({ retailInventoryId: null });
      jest.spyOn(retailInventoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailInventory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailInventory }));
      saveSubject.complete();

      // THEN
      expect(retailInventoryFormService.getRetailInventory).toHaveBeenCalled();
      expect(retailInventoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailInventory>>();
      const retailInventory = { retailInventoryId: 123 };
      jest.spyOn(retailInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(retailInventoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRetailer', () => {
      it('Should forward to retailerService', () => {
        const entity = { retailerId: 123 };
        const entity2 = { retailerId: 456 };
        jest.spyOn(retailerService, 'compareRetailer');
        comp.compareRetailer(entity, entity2);
        expect(retailerService.compareRetailer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
