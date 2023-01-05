import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRetailerUser } from '../retailer-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../retailer-user.test-samples';

import { RetailerUserService, RestRetailerUser } from './retailer-user.service';

const requireRestSample: RestRetailerUser = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
};

describe('RetailerUser Service', () => {
  let service: RetailerUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IRetailerUser | IRetailerUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RetailerUserService);
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

    it('should create a RetailerUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const retailerUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(retailerUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RetailerUser', () => {
      const retailerUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(retailerUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RetailerUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RetailerUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RetailerUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRetailerUserToCollectionIfMissing', () => {
      it('should add a RetailerUser to an empty array', () => {
        const retailerUser: IRetailerUser = sampleWithRequiredData;
        expectedResult = service.addRetailerUserToCollectionIfMissing([], retailerUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailerUser);
      });

      it('should not add a RetailerUser to an array that contains it', () => {
        const retailerUser: IRetailerUser = sampleWithRequiredData;
        const retailerUserCollection: IRetailerUser[] = [
          {
            ...retailerUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRetailerUserToCollectionIfMissing(retailerUserCollection, retailerUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RetailerUser to an array that doesn't contain it", () => {
        const retailerUser: IRetailerUser = sampleWithRequiredData;
        const retailerUserCollection: IRetailerUser[] = [sampleWithPartialData];
        expectedResult = service.addRetailerUserToCollectionIfMissing(retailerUserCollection, retailerUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailerUser);
      });

      it('should add only unique RetailerUser to an array', () => {
        const retailerUserArray: IRetailerUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const retailerUserCollection: IRetailerUser[] = [sampleWithRequiredData];
        expectedResult = service.addRetailerUserToCollectionIfMissing(retailerUserCollection, ...retailerUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const retailerUser: IRetailerUser = sampleWithRequiredData;
        const retailerUser2: IRetailerUser = sampleWithPartialData;
        expectedResult = service.addRetailerUserToCollectionIfMissing([], retailerUser, retailerUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retailerUser);
        expect(expectedResult).toContain(retailerUser2);
      });

      it('should accept null and undefined values', () => {
        const retailerUser: IRetailerUser = sampleWithRequiredData;
        expectedResult = service.addRetailerUserToCollectionIfMissing([], null, retailerUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retailerUser);
      });

      it('should return initial array if no RetailerUser is added', () => {
        const retailerUserCollection: IRetailerUser[] = [sampleWithRequiredData];
        expectedResult = service.addRetailerUserToCollectionIfMissing(retailerUserCollection, undefined, null);
        expect(expectedResult).toEqual(retailerUserCollection);
      });
    });

    describe('compareRetailerUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRetailerUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { retailerUserId: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRetailerUser(entity1, entity2);
        const compareResult2 = service.compareRetailerUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { retailerUserId: 123 };
        const entity2 = { retailerUserId: 456 };

        const compareResult1 = service.compareRetailerUser(entity1, entity2);
        const compareResult2 = service.compareRetailerUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { retailerUserId: 123 };
        const entity2 = { retailerUserId: 123 };

        const compareResult1 = service.compareRetailerUser(entity1, entity2);
        const compareResult2 = service.compareRetailerUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
