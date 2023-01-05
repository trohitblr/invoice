import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRetailInventory, NewRetailInventory } from '../retail-inventory.model';

export type PartialUpdateRetailInventory = Partial<IRetailInventory> & Pick<IRetailInventory, 'retailInventoryId'>;

type RestOf<T extends IRetailInventory | NewRetailInventory> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestRetailInventory = RestOf<IRetailInventory>;

export type NewRestRetailInventory = RestOf<NewRetailInventory>;

export type PartialUpdateRestRetailInventory = RestOf<PartialUpdateRetailInventory>;

export type EntityResponseType = HttpResponse<IRetailInventory>;
export type EntityArrayResponseType = HttpResponse<IRetailInventory[]>;

@Injectable({ providedIn: 'root' })
export class RetailInventoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/retail-inventories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(retailInventory: NewRetailInventory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailInventory);
    return this.http
      .post<RestRetailInventory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(retailInventory: IRetailInventory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailInventory);
    return this.http
      .put<RestRetailInventory>(`${this.resourceUrl}/${this.getRetailInventoryIdentifier(retailInventory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(retailInventory: PartialUpdateRetailInventory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailInventory);
    return this.http
      .patch<RestRetailInventory>(`${this.resourceUrl}/${this.getRetailInventoryIdentifier(retailInventory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRetailInventory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRetailInventory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRetailInventoryIdentifier(retailInventory: Pick<IRetailInventory, 'retailInventoryId'>): number {
    return retailInventory.retailInventoryId;
  }

  compareRetailInventory(
    o1: Pick<IRetailInventory, 'retailInventoryId'> | null,
    o2: Pick<IRetailInventory, 'retailInventoryId'> | null
  ): boolean {
    return o1 && o2 ? this.getRetailInventoryIdentifier(o1) === this.getRetailInventoryIdentifier(o2) : o1 === o2;
  }

  addRetailInventoryToCollectionIfMissing<Type extends Pick<IRetailInventory, 'retailInventoryId'>>(
    retailInventoryCollection: Type[],
    ...retailInventoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const retailInventories: Type[] = retailInventoriesToCheck.filter(isPresent);
    if (retailInventories.length > 0) {
      const retailInventoryCollectionIdentifiers = retailInventoryCollection.map(
        retailInventoryItem => this.getRetailInventoryIdentifier(retailInventoryItem)!
      );
      const retailInventoriesToAdd = retailInventories.filter(retailInventoryItem => {
        const retailInventoryIdentifier = this.getRetailInventoryIdentifier(retailInventoryItem);
        if (retailInventoryCollectionIdentifiers.includes(retailInventoryIdentifier)) {
          return false;
        }
        retailInventoryCollectionIdentifiers.push(retailInventoryIdentifier);
        return true;
      });
      return [...retailInventoriesToAdd, ...retailInventoryCollection];
    }
    return retailInventoryCollection;
  }

  protected convertDateFromClient<T extends IRetailInventory | NewRetailInventory | PartialUpdateRetailInventory>(
    retailInventory: T
  ): RestOf<T> {
    return {
      ...retailInventory,
      createdOn: retailInventory.createdOn?.toJSON() ?? null,
      updatedOn: retailInventory.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRetailInventory: RestRetailInventory): IRetailInventory {
    return {
      ...restRetailInventory,
      createdOn: restRetailInventory.createdOn ? dayjs(restRetailInventory.createdOn) : undefined,
      updatedOn: restRetailInventory.updatedOn ? dayjs(restRetailInventory.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRetailInventory>): HttpResponse<IRetailInventory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRetailInventory[]>): HttpResponse<IRetailInventory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
