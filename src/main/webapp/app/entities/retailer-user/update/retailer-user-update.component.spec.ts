import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RetailerUserFormService } from './retailer-user-form.service';
import { RetailerUserService } from '../service/retailer-user.service';
import { IRetailerUser } from '../retailer-user.model';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';

import { RetailerUserUpdateComponent } from './retailer-user-update.component';

describe('RetailerUser Management Update Component', () => {
  let comp: RetailerUserUpdateComponent;
  let fixture: ComponentFixture<RetailerUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let retailerUserFormService: RetailerUserFormService;
  let retailerUserService: RetailerUserService;
  let retailerService: RetailerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RetailerUserUpdateComponent],
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
      .overrideTemplate(RetailerUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailerUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    retailerUserFormService = TestBed.inject(RetailerUserFormService);
    retailerUserService = TestBed.inject(RetailerUserService);
    retailerService = TestBed.inject(RetailerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Retailer query and add missing value', () => {
      const retailerUser: IRetailerUser = { retailerUserId: 456 };
      const retailers: IRetailer = { retailerId: 73689 };
      retailerUser.retailers = retailers;

      const retailerCollection: IRetailer[] = [{ retailerId: 48818 }];
      jest.spyOn(retailerService, 'query').mockReturnValue(of(new HttpResponse({ body: retailerCollection })));
      const additionalRetailers = [retailers];
      const expectedCollection: IRetailer[] = [...additionalRetailers, ...retailerCollection];
      jest.spyOn(retailerService, 'addRetailerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ retailerUser });
      comp.ngOnInit();

      expect(retailerService.query).toHaveBeenCalled();
      expect(retailerService.addRetailerToCollectionIfMissing).toHaveBeenCalledWith(
        retailerCollection,
        ...additionalRetailers.map(expect.objectContaining)
      );
      expect(comp.retailersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const retailerUser: IRetailerUser = { retailerUserId: 456 };
      const retailers: IRetailer = { retailerId: 43717 };
      retailerUser.retailers = retailers;

      activatedRoute.data = of({ retailerUser });
      comp.ngOnInit();

      expect(comp.retailersSharedCollection).toContain(retailers);
      expect(comp.retailerUser).toEqual(retailerUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailerUser>>();
      const retailerUser = { retailerUserId: 123 };
      jest.spyOn(retailerUserFormService, 'getRetailerUser').mockReturnValue(retailerUser);
      jest.spyOn(retailerUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailerUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailerUser }));
      saveSubject.complete();

      // THEN
      expect(retailerUserFormService.getRetailerUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(retailerUserService.update).toHaveBeenCalledWith(expect.objectContaining(retailerUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailerUser>>();
      const retailerUser = { retailerUserId: 123 };
      jest.spyOn(retailerUserFormService, 'getRetailerUser').mockReturnValue({ retailerUserId: null });
      jest.spyOn(retailerUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailerUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailerUser }));
      saveSubject.complete();

      // THEN
      expect(retailerUserFormService.getRetailerUser).toHaveBeenCalled();
      expect(retailerUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailerUser>>();
      const retailerUser = { retailerUserId: 123 };
      jest.spyOn(retailerUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailerUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(retailerUserService.update).toHaveBeenCalled();
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
