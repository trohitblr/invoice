import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRetailInventory } from '../retail-inventory.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../retail-inventory.test-samples';

import { RetailInventoryService, RestRetailInventory } from './retail-inventory.service';

const requireRestSample: RestRetailInventory = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
};

describe('RetailInventory Service', () => {
  let service: RetailInventoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IRetailInventory | IRetailInventory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RetailInventoryService);
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

    it('should create a RetailInventory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const retailInventory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(retailInventory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RetailInventory', () => {
      const retailInventory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(retailInventory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RetailInventory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RetailInventory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RetailInventory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRetailInventoryToCollectionIfMissing', () => {
      it('should add a RetailInventory to an empty array', () => {
        const retailInventory: IRetailInventory = sampleWithRequiredData;
        expectedResult = service.addRetailInventoryToCollectionIfMissing([], retailInventory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailInventory);
      });

      it('should not add a RetailInventory to an array that contains it', () => {
        const retailInventory: IRetailInventory = sampleWithRequiredData;
        const retailInventoryCollection: IRetailInventory[] = [
          {
            ...retailInventory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRetailInventoryToCollectionIfMissing(retailInventoryCollection, retailInventory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RetailInventory to an array that doesn't contain it", () => {
        const retailInventory: IRetailInventory = sampleWithRequiredData;
        const retailInventoryCollection: IRetailInventory[] = [sampleWithPartialData];
        expectedResult = service.addRetailInventoryToCollectionIfMissing(retailInventoryCollection, retailInventory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailInventory);
      });

      it('should add only unique RetailInventory to an array', () => {
        const retailInventoryArray: IRetailInventory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const retailInventoryCollection: IRetailInventory[] = [sampleWithRequiredData];
        expectedResult = service.addRetailInventoryToCollectionIfMissing(retailInventoryCollection, ...retailInventoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const retailInventory: IRetailInventory = sampleWithRequiredData;
        const retailInventory2: IRetailInventory = sampleWithPartialData;
        expectedResult = service.addRetailInventoryToCollectionIfMissing([], retailInventory, retailInventory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailInventory);
        expect(expectedResult).toContain(retailInventory2);
      });

      it('should accept null and undefined values', () => {
        const retailInventory: IRetailInventory = sampleWithRequiredData;
        expectedResult = service.addRetailInventoryToCollectionIfMissing([], null, retailInventory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailInventory);
      });

      it('should return initial array if no RetailInventory is added', () => {
        const retailInventoryCollection: IRetailInventory[] = [sampleWithRequiredData];
        expectedResult = service.addRetailInventoryToCollectionIfMissing(retailInventoryCollection, undefined, null);
        expect(expectedResult).toEqual(retailInventoryCollection);
      });
    });

    describe('compareRetailInventory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRetailInventory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { retailInventoryId: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRetailInventory(entity1, entity2);
        const compareResult2 = service.compareRetailInventory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { retailInventoryId: 123 };
        const entity2 = { retailInventoryId: 456 };

        const compareResult1 = service.compareRetailInventory(entity1, entity2);
        const compareResult2 = service.compareRetailInventory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { retailInventoryId: 123 };
        const entity2 = { retailInventoryId: 123 };

        const compareResult1 = service.compareRetailInventory(entity1, entity2);
        const compareResult2 = service.compareRetailInventory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
