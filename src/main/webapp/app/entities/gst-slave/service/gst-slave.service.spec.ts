import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGstSlave } from '../gst-slave.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../gst-slave.test-samples';

import { GstSlaveService } from './gst-slave.service';

const requireRestSample: IGstSlave = {
  ...sampleWithRequiredData,
};

describe('GstSlave Service', () => {
  let service: GstSlaveService;
  let httpMock: HttpTestingController;
  let expectedResult: IGstSlave | IGstSlave[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GstSlaveService);
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

    it('should create a GstSlave', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gstSlave = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gstSlave).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GstSlave', () => {
      const gstSlave = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gstSlave).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GstSlave', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GstSlave', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GstSlave', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGstSlaveToCollectionIfMissing', () => {
      it('should add a GstSlave to an empty array', () => {
        const gstSlave: IGstSlave = sampleWithRequiredData;
        expectedResult = service.addGstSlaveToCollectionIfMissing([], gstSlave);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gstSlave);
      });

      it('should not add a GstSlave to an array that contains it', () => {
        const gstSlave: IGstSlave = sampleWithRequiredData;
        const gstSlaveCollection: IGstSlave[] = [
          {
            ...gstSlave,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGstSlaveToCollectionIfMissing(gstSlaveCollection, gstSlave);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GstSlave to an array that doesn't contain it", () => {
        const gstSlave: IGstSlave = sampleWithRequiredData;
        const gstSlaveCollection: IGstSlave[] = [sampleWithPartialData];
        expectedResult = service.addGstSlaveToCollectionIfMissing(gstSlaveCollection, gstSlave);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gstSlave);
      });

      it('should add only unique GstSlave to an array', () => {
        const gstSlaveArray: IGstSlave[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gstSlaveCollection: IGstSlave[] = [sampleWithRequiredData];
        expectedResult = service.addGstSlaveToCollectionIfMissing(gstSlaveCollection, ...gstSlaveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gstSlave: IGstSlave = sampleWithRequiredData;
        const gstSlave2: IGstSlave = sampleWithPartialData;
        expectedResult = service.addGstSlaveToCollectionIfMissing([], gstSlave, gstSlave2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gstSlave);
        expect(expectedResult).toContain(gstSlave2);
      });

      it('should accept null and undefined values', () => {
        const gstSlave: IGstSlave = sampleWithRequiredData;
        expectedResult = service.addGstSlaveToCollectionIfMissing([], null, gstSlave, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gstSlave);
      });

      it('should return initial array if no GstSlave is added', () => {
        const gstSlaveCollection: IGstSlave[] = [sampleWithRequiredData];
        expectedResult = service.addGstSlaveToCollectionIfMissing(gstSlaveCollection, undefined, null);
        expect(expectedResult).toEqual(gstSlaveCollection);
      });
    });

    describe('compareGstSlave', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGstSlave(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { gstSlaveId: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGstSlave(entity1, entity2);
        const compareResult2 = service.compareGstSlave(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { gstSlaveId: 123 };
        const entity2 = { gstSlaveId: 456 };

        const compareResult1 = service.compareGstSlave(entity1, entity2);
        const compareResult2 = service.compareGstSlave(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { gstSlaveId: 123 };
        const entity2 = { gstSlaveId: 123 };

        const compareResult1 = service.compareGstSlave(entity1, entity2);
        const compareResult2 = service.compareGstSlave(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
