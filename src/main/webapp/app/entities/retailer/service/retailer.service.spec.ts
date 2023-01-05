import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRetailer } from '../retailer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../retailer.test-samples';

import { RetailerService, RestRetailer } from './retailer.service';

const requireRestSample: RestRetailer = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
};

describe('Retailer Service', () => {
  let service: RetailerService;
  let httpMock: HttpTestingController;
  let expectedResult: IRetailer | IRetailer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RetailerService);
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

    it('should create a Retailer', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const retailer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(retailer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Retailer', () => {
      const retailer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(retailer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Retailer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Retailer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Retailer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRetailerToCollectionIfMissing', () => {
      it('should add a Retailer to an empty array', () => {
        const retailer: IRetailer = sampleWithRequiredData;
        expectedResult = service.addRetailerToCollectionIfMissing([], retailer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailer);
      });

      it('should not add a Retailer to an array that contains it', () => {
        const retailer: IRetailer = sampleWithRequiredData;
        const retailerCollection: IRetailer[] = [
          {
            ...retailer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, retailer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Retailer to an array that doesn't contain it", () => {
        const retailer: IRetailer = sampleWithRequiredData;
        const retailerCollection: IRetailer[] = [sampleWithPartialData];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, retailer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailer);
      });

      it('should add only unique Retailer to an array', () => {
        const retailerArray: IRetailer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const retailerCollection: IRetailer[] = [sampleWithRequiredData];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, ...retailerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const retailer: IRetailer = sampleWithRequiredData;
        const retailer2: IRetailer = sampleWithPartialData;
        expectedResult = service.addRetailerToCollectionIfMissing([], retailer, retailer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailer);
        expect(expectedResult).toContain(retailer2);
      });

      it('should accept null and undefined values', () => {
        const retailer: IRetailer = sampleWithRequiredData;
        expectedResult = service.addRetailerToCollectionIfMissing([], null, retailer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailer);
      });

      it('should return initial array if no Retailer is added', () => {
        const retailerCollection: IRetailer[] = [sampleWithRequiredData];
        expectedResult = service.addRetailerToCollectionIfMissing(retailerCollection, undefined, null);
        expect(expectedResult).toEqual(retailerCollection);
      });
    });

    describe('compareRetailer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRetailer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { retailerId: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRetailer(entity1, entity2);
        const compareResult2 = service.compareRetailer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { retailerId: 123 };
        const entity2 = { retailerId: 456 };

        const compareResult1 = service.compareRetailer(entity1, entity2);
        const compareResult2 = service.compareRetailer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { retailerId: 123 };
        const entity2 = { retailerId: 123 };

        const compareResult1 = service.compareRetailer(entity1, entity2);
        const compareResult2 = service.compareRetailer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
