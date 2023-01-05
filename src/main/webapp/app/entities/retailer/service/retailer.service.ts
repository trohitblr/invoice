import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRetailer, NewRetailer } from '../retailer.model';

export type PartialUpdateRetailer = Partial<IRetailer> & Pick<IRetailer, 'retailerId'>;

type RestOf<T extends IRetailer | NewRetailer> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestRetailer = RestOf<IRetailer>;

export type NewRestRetailer = RestOf<NewRetailer>;

export type PartialUpdateRestRetailer = RestOf<PartialUpdateRetailer>;

export type EntityResponseType = HttpResponse<IRetailer>;
export type EntityArrayResponseType = HttpResponse<IRetailer[]>;

@Injectable({ providedIn: 'root' })
export class RetailerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/retailers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(retailer: NewRetailer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailer);
    return this.http
      .post<RestRetailer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(retailer: IRetailer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailer);
    return this.http
      .put<RestRetailer>(`${this.resourceUrl}/${this.getRetailerIdentifier(retailer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(retailer: PartialUpdateRetailer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailer);
    return this.http
      .patch<RestRetailer>(`${this.resourceUrl}/${this.getRetailerIdentifier(retailer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRetailer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRetailer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRetailerIdentifier(retailer: Pick<IRetailer, 'retailerId'>): number {
    return retailer.retailerId;
  }

  compareRetailer(o1: Pick<IRetailer, 'retailerId'> | null, o2: Pick<IRetailer, 'retailerId'> | null): boolean {
    return o1 && o2 ? this.getRetailerIdentifier(o1) === this.getRetailerIdentifier(o2) : o1 === o2;
  }

  addRetailerToCollectionIfMissing<Type extends Pick<IRetailer, 'retailerId'>>(
    retailerCollection: Type[],
    ...retailersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const retailers: Type[] = retailersToCheck.filter(isPresent);
    if (retailers.length > 0) {
      const retailerCollectionIdentifiers = retailerCollection.map(retailerItem => this.getRetailerIdentifier(retailerItem)!);
      const retailersToAdd = retailers.filter(retailerItem => {
        const retailerIdentifier = this.getRetailerIdentifier(retailerItem);
        if (retailerCollectionIdentifiers.includes(retailerIdentifier)) {
          return false;
        }
        retailerCollectionIdentifiers.push(retailerIdentifier);
        return true;
      });
      return [...retailersToAdd, ...retailerCollection];
    }
    return retailerCollection;
  }

  protected convertDateFromClient<T extends IRetailer | NewRetailer | PartialUpdateRetailer>(retailer: T): RestOf<T> {
    return {
      ...retailer,
      createdOn: retailer.createdOn?.toJSON() ?? null,
      updatedOn: retailer.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRetailer: RestRetailer): IRetailer {
    return {
      ...restRetailer,
      createdOn: restRetailer.createdOn ? dayjs(restRetailer.createdOn) : undefined,
      updatedOn: restRetailer.updatedOn ? dayjs(restRetailer.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRetailer>): HttpResponse<IRetailer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRetailer[]>): HttpResponse<IRetailer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
