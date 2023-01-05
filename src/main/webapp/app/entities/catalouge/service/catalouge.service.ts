import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalouge, NewCatalouge } from '../catalouge.model';

export type PartialUpdateCatalouge = Partial<ICatalouge> & Pick<ICatalouge, 'catalougeId'>;

type RestOf<T extends ICatalouge | NewCatalouge> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestCatalouge = RestOf<ICatalouge>;

export type NewRestCatalouge = RestOf<NewCatalouge>;

export type PartialUpdateRestCatalouge = RestOf<PartialUpdateCatalouge>;

export type EntityResponseType = HttpResponse<ICatalouge>;
export type EntityArrayResponseType = HttpResponse<ICatalouge[]>;

@Injectable({ providedIn: 'root' })
export class CatalougeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalouges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalouge: NewCatalouge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalouge);
    return this.http
      .post<RestCatalouge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(catalouge: ICatalouge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalouge);
    return this.http
      .put<RestCatalouge>(`${this.resourceUrl}/${this.getCatalougeIdentifier(catalouge)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(catalouge: PartialUpdateCatalouge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalouge);
    return this.http
      .patch<RestCatalouge>(`${this.resourceUrl}/${this.getCatalougeIdentifier(catalouge)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCatalouge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCatalouge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCatalougeIdentifier(catalouge: Pick<ICatalouge, 'catalougeId'>): number {
    return catalouge.catalougeId;
  }

  compareCatalouge(o1: Pick<ICatalouge, 'catalougeId'> | null, o2: Pick<ICatalouge, 'catalougeId'> | null): boolean {
    return o1 && o2 ? this.getCatalougeIdentifier(o1) === this.getCatalougeIdentifier(o2) : o1 === o2;
  }

  addCatalougeToCollectionIfMissing<Type extends Pick<ICatalouge, 'catalougeId'>>(
    catalougeCollection: Type[],
    ...catalougesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const catalouges: Type[] = catalougesToCheck.filter(isPresent);
    if (catalouges.length > 0) {
      const catalougeCollectionIdentifiers = catalougeCollection.map(catalougeItem => this.getCatalougeIdentifier(catalougeItem)!);
      const catalougesToAdd = catalouges.filter(catalougeItem => {
        const catalougeIdentifier = this.getCatalougeIdentifier(catalougeItem);
        if (catalougeCollectionIdentifiers.includes(catalougeIdentifier)) {
          return false;
        }
        catalougeCollectionIdentifiers.push(catalougeIdentifier);
        return true;
      });
      return [...catalougesToAdd, ...catalougeCollection];
    }
    return catalougeCollection;
  }

  protected convertDateFromClient<T extends ICatalouge | NewCatalouge | PartialUpdateCatalouge>(catalouge: T): RestOf<T> {
    return {
      ...catalouge,
      createdOn: catalouge.createdOn?.toJSON() ?? null,
      updatedOn: catalouge.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCatalouge: RestCatalouge): ICatalouge {
    return {
      ...restCatalouge,
      createdOn: restCatalouge.createdOn ? dayjs(restCatalouge.createdOn) : undefined,
      updatedOn: restCatalouge.updatedOn ? dayjs(restCatalouge.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCatalouge>): HttpResponse<ICatalouge> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCatalouge[]>): HttpResponse<ICatalouge[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
