import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GstSlaveFormService } from './gst-slave-form.service';
import { GstSlaveService } from '../service/gst-slave.service';
import { IGstSlave } from '../gst-slave.model';

import { GstSlaveUpdateComponent } from './gst-slave-update.component';

describe('GstSlave Management Update Component', () => {
  let comp: GstSlaveUpdateComponent;
  let fixture: ComponentFixture<GstSlaveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gstSlaveFormService: GstSlaveFormService;
  let gstSlaveService: GstSlaveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GstSlaveUpdateComponent],
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
      .overrideTemplate(GstSlaveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GstSlaveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gstSlaveFormService = TestBed.inject(GstSlaveFormService);
    gstSlaveService = TestBed.inject(GstSlaveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gstSlave: IGstSlave = { gstSlaveId: 456 };

      activatedRoute.data = of({ gstSlave });
      comp.ngOnInit();

      expect(comp.gstSlave).toEqual(gstSlave);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGstSlave>>();
      const gstSlave = { gstSlaveId: 123 };
      jest.spyOn(gstSlaveFormService, 'getGstSlave').mockReturnValue(gstSlave);
      jest.spyOn(gstSlaveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gstSlave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gstSlave }));
      saveSubject.complete();

      // THEN
      expect(gstSlaveFormService.getGstSlave).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gstSlaveService.update).toHaveBeenCalledWith(expect.objectContaining(gstSlave));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGstSlave>>();
      const gstSlave = { gstSlaveId: 123 };
      jest.spyOn(gstSlaveFormService, 'getGstSlave').mockReturnValue({ gstSlaveId: null });
      jest.spyOn(gstSlaveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gstSlave: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gstSlave }));
      saveSubject.complete();

      // THEN
      expect(gstSlaveFormService.getGstSlave).toHaveBeenCalled();
      expect(gstSlaveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGstSlave>>();
      const gstSlave = { gstSlaveId: 123 };
      jest.spyOn(gstSlaveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gstSlave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gstSlaveService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
