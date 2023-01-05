import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInvoiceLineItem } from '../invoice-line-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../invoice-line-item.test-samples';

import { InvoiceLineItemService, RestInvoiceLineItem } from './invoice-line-item.service';

const requireRestSample: RestInvoiceLineItem = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
};

describe('InvoiceLineItem Service', () => {
  let service: InvoiceLineItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceLineItem | IInvoiceLineItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceLineItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a InvoiceLineItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const invoiceLineItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceLineItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceLineItem', () => {
      const invoiceLineItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceLineItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceLineItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceLineItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceLineItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInvoiceLineItemToCollectionIfMissing', () => {
      it('should add a InvoiceLineItem to an empty array', () => {
        const invoiceLineItem: IInvoiceLineItem = sampleWithRequiredData;
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing([], invoiceLineItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceLineItem);
      });

      it('should not add a InvoiceLineItem to an array that contains it', () => {
        const invoiceLineItem: IInvoiceLineItem = sampleWithRequiredData;
        const invoiceLineItemCollection: IInvoiceLineItem[] = [
          {
            ...invoiceLineItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing(invoiceLineItemCollection, invoiceLineItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceLineItem to an array that doesn't contain it", () => {
        const invoiceLineItem: IInvoiceLineItem = sampleWithRequiredData;
        const invoiceLineItemCollection: IInvoiceLineItem[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing(invoiceLineItemCollection, invoiceLineItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceLineItem);
      });

      it('should add only unique InvoiceLineItem to an array', () => {
        const invoiceLineItemArray: IInvoiceLineItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceLineItemCollection: IInvoiceLineItem[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing(invoiceLineItemCollection, ...invoiceLineItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceLineItem: IInvoiceLineItem = sampleWithRequiredData;
        const invoiceLineItem2: IInvoiceLineItem = sampleWithPartialData;
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing([], invoiceLineItem, invoiceLineItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceLineItem);
        expect(expectedResult).toContain(invoiceLineItem2);
      });

      it('should accept null and undefined values', () => {
        const invoiceLineItem: IInvoiceLineItem = sampleWithRequiredData;
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing([], null, invoiceLineItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceLineItem);
      });

      it('should return initial array if no InvoiceLineItem is added', () => {
        const invoiceLineItemCollection: IInvoiceLineItem[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceLineItemToCollectionIfMissing(invoiceLineItemCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceLineItemCollection);
      });
    });

    describe('compareInvoiceLineItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceLineItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { invoiceLineItemId: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceLineItem(entity1, entity2);
        const compareResult2 = service.compareInvoiceLineItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { invoiceLineItemId: 123 };
        const entity2 = { invoiceLineItemId: 456 };

        const compareResult1 = service.compareInvoiceLineItem(entity1, entity2);
        const compareResult2 = service.compareInvoiceLineItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { invoiceLineItemId: 123 };
        const entity2 = { invoiceLineItemId: 123 };

        const compareResult1 = service.compareInvoiceLineItem(entity1, entity2);
        const compareResult2 = service.compareInvoiceLineItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
