import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalouge } from '../catalouge.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../catalouge.test-samples';

import { CatalougeService, RestCatalouge } from './catalouge.service';

const requireRestSample: RestCatalouge = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
};

describe('Catalouge Service', () => {
  let service: CatalougeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICatalouge | ICatalouge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CatalougeService);
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

    it('should create a Catalouge', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const catalouge = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(catalouge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Catalouge', () => {
      const catalouge = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(catalouge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Catalouge', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Catalouge', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Catalouge', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCatalougeToCollectionIfMissing', () => {
      it('should add a Catalouge to an empty array', () => {
        const catalouge: ICatalouge = sampleWithRequiredData;
        expectedResult = service.addCatalougeToCollectionIfMissing([], catalouge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalouge);
      });

      it('should not add a Catalouge to an array that contains it', () => {
        const catalouge: ICatalouge = sampleWithRequiredData;
        const catalougeCollection: ICatalouge[] = [
          {
            ...catalouge,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCatalougeToCollectionIfMissing(catalougeCollection, catalouge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Catalouge to an array that doesn't contain it", () => {
        const catalouge: ICatalouge = sampleWithRequiredData;
        const catalougeCollection: ICatalouge[] = [sampleWithPartialData];
        expectedResult = service.addCatalougeToCollectionIfMissing(catalougeCollection, catalouge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalouge);
      });

      it('should add only unique Catalouge to an array', () => {
        const catalougeArray: ICatalouge[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const catalougeCollection: ICatalouge[] = [sampleWithRequiredData];
        expectedResult = service.addCatalougeToCollectionIfMissing(catalougeCollection, ...catalougeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalouge: ICatalouge = sampleWithRequiredData;
        const catalouge2: ICatalouge = sampleWithPartialData;
        expectedResult = service.addCatalougeToCollectionIfMissing([], catalouge, catalouge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalouge);
        expect(expectedResult).toContain(catalouge2);
      });

      it('should accept null and undefined values', () => {
        const catalouge: ICatalouge = sampleWithRequiredData;
        expectedResult = service.addCatalougeToCollectionIfMissing([], null, catalouge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalouge);
      });

      it('should return initial array if no Catalouge is added', () => {
        const catalougeCollection: ICatalouge[] = [sampleWithRequiredData];
        expectedResult = service.addCatalougeToCollectionIfMissing(catalougeCollection, undefined, null);
        expect(expectedResult).toEqual(catalougeCollection);
      });
    });

    describe('compareCatalouge', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCatalouge(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { catalougeId: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCatalouge(entity1, entity2);
        const compareResult2 = service.compareCatalouge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { catalougeId: 123 };
        const entity2 = { catalougeId: 456 };

        const compareResult1 = service.compareCatalouge(entity1, entity2);
        const compareResult2 = service.compareCatalouge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { catalougeId: 123 };
        const entity2 = { catalougeId: 123 };

        const compareResult1 = service.compareCatalouge(entity1, entity2);
        const compareResult2 = service.compareCatalouge(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
