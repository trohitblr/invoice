import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RetailerFormService } from './retailer-form.service';
import { RetailerService } from '../service/retailer.service';
import { IRetailer } from '../retailer.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IMedia } from 'app/entities/media/media.model';
import { MediaService } from 'app/entities/media/service/media.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { RetailerUpdateComponent } from './retailer-update.component';

describe('Retailer Management Update Component', () => {
  let comp: RetailerUpdateComponent;
  let fixture: ComponentFixture<RetailerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let retailerFormService: RetailerFormService;
  let retailerService: RetailerService;
  let addressService: AddressService;
  let mediaService: MediaService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RetailerUpdateComponent],
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
      .overrideTemplate(RetailerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetailerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    retailerFormService = TestBed.inject(RetailerFormService);
    retailerService = TestBed.inject(RetailerService);
    addressService = TestBed.inject(AddressService);
    mediaService = TestBed.inject(MediaService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Address query and add missing value', () => {
      const retailer: IRetailer = { retailerId: 456 };
      const invoiceAddress: IAddress = { id: 49803 };
      retailer.invoiceAddress = invoiceAddress;
      const billingAddress: IAddress = { id: 82147 };
      retailer.billingAddress = billingAddress;

      const addressCollection: IAddress[] = [{ id: 47166 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const additionalAddresses = [invoiceAddress, billingAddress];
      const expectedCollection: IAddress[] = [...additionalAddresses, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(
        addressCollection,
        ...additionalAddresses.map(expect.objectContaining)
      );
      expect(comp.addressesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Media query and add missing value', () => {
      const retailer: IRetailer = { retailerId: 456 };
      const logo: IMedia = { mediaId: 10689 };
      retailer.logo = logo;

      const mediaCollection: IMedia[] = [{ mediaId: 42215 }];
      jest.spyOn(mediaService, 'query').mockReturnValue(of(new HttpResponse({ body: mediaCollection })));
      const additionalMedia = [logo];
      const expectedCollection: IMedia[] = [...additionalMedia, ...mediaCollection];
      jest.spyOn(mediaService, 'addMediaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      expect(mediaService.query).toHaveBeenCalled();
      expect(mediaService.addMediaToCollectionIfMissing).toHaveBeenCalledWith(
        mediaCollection,
        ...additionalMedia.map(expect.objectContaining)
      );
      expect(comp.mediaSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const retailer: IRetailer = { retailerId: 456 };
      const customers: ICustomer[] = [{ customerId: 69329 }];
      retailer.customers = customers;

      const customerCollection: ICustomer[] = [{ customerId: 43646 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [...customers];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const retailer: IRetailer = { retailerId: 456 };
      const invoiceAddress: IAddress = { id: 43661 };
      retailer.invoiceAddress = invoiceAddress;
      const billingAddress: IAddress = { id: 51825 };
      retailer.billingAddress = billingAddress;
      const logo: IMedia = { mediaId: 83023 };
      retailer.logo = logo;
      const customers: ICustomer = { customerId: 77205 };
      retailer.customers = [customers];

      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      expect(comp.addressesSharedCollection).toContain(invoiceAddress);
      expect(comp.addressesSharedCollection).toContain(billingAddress);
      expect(comp.mediaSharedCollection).toContain(logo);
      expect(comp.customersSharedCollection).toContain(customers);
      expect(comp.retailer).toEqual(retailer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailer>>();
      const retailer = { retailerId: 123 };
      jest.spyOn(retailerFormService, 'getRetailer').mockReturnValue(retailer);
      jest.spyOn(retailerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailer }));
      saveSubject.complete();

      // THEN
      expect(retailerFormService.getRetailer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(retailerService.update).toHaveBeenCalledWith(expect.objectContaining(retailer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailer>>();
      const retailer = { retailerId: 123 };
      jest.spyOn(retailerFormService, 'getRetailer').mockReturnValue({ retailerId: null });
      jest.spyOn(retailerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retailer }));
      saveSubject.complete();

      // THEN
      expect(retailerFormService.getRetailer).toHaveBeenCalled();
      expect(retailerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetailer>>();
      const retailer = { retailerId: 123 };
      jest.spyOn(retailerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retailer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(retailerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAddress', () => {
      it('Should forward to addressService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(addressService, 'compareAddress');
        comp.compareAddress(entity, entity2);
        expect(addressService.compareAddress).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMedia', () => {
      it('Should forward to mediaService', () => {
        const entity = { mediaId: 123 };
        const entity2 = { mediaId: 456 };
        jest.spyOn(mediaService, 'compareMedia');
        comp.compareMedia(entity, entity2);
        expect(mediaService.compareMedia).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { customerId: 123 };
        const entity2 = { customerId: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
