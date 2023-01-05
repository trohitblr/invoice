import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategories, NewCategories } from '../categories.model';

export type PartialUpdateCategories = Partial<ICategories> & Pick<ICategories, 'categoryId'>;

type RestOf<T extends ICategories | NewCategories> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestCategories = RestOf<ICategories>;

export type NewRestCategories = RestOf<NewCategories>;

export type PartialUpdateRestCategories = RestOf<PartialUpdateCategories>;

export type EntityResponseType = HttpResponse<ICategories>;
export type EntityArrayResponseType = HttpResponse<ICategories[]>;

@Injectable({ providedIn: 'root' })
export class CategoriesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categories: NewCategories): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categories);
    return this.http
      .post<RestCategories>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(categories: ICategories): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categories);
    return this.http
      .put<RestCategories>(`${this.resourceUrl}/${this.getCategoriesIdentifier(categories)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(categories: PartialUpdateCategories): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categories);
    return this.http
      .patch<RestCategories>(`${this.resourceUrl}/${this.getCategoriesIdentifier(categories)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCategories>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCategories[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategoriesIdentifier(categories: Pick<ICategories, 'categoryId'>): number {
    return categories.categoryId;
  }

  compareCategories(o1: Pick<ICategories, 'categoryId'> | null, o2: Pick<ICategories, 'categoryId'> | null): boolean {
    return o1 && o2 ? this.getCategoriesIdentifier(o1) === this.getCategoriesIdentifier(o2) : o1 === o2;
  }

  addCategoriesToCollectionIfMissing<Type extends Pick<ICategories, 'categoryId'>>(
    categoriesCollection: Type[],
    ...categoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categories: Type[] = categoriesToCheck.filter(isPresent);
    if (categories.length > 0) {
      const categoriesCollectionIdentifiers = categoriesCollection.map(categoriesItem => this.getCategoriesIdentifier(categoriesItem)!);
      const categoriesToAdd = categories.filter(categoriesItem => {
        const categoriesIdentifier = this.getCategoriesIdentifier(categoriesItem);
        if (categoriesCollectionIdentifiers.includes(categoriesIdentifier)) {
          return false;
        }
        categoriesCollectionIdentifiers.push(categoriesIdentifier);
        return true;
      });
      return [...categoriesToAdd, ...categoriesCollection];
    }
    return categoriesCollection;
  }

  protected convertDateFromClient<T extends ICategories | NewCategories | PartialUpdateCategories>(categories: T): RestOf<T> {
    return {
      ...categories,
      createdOn: categories.createdOn?.toJSON() ?? null,
      updatedOn: categories.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCategories: RestCategories): ICategories {
    return {
      ...restCategories,
      createdOn: restCategories.createdOn ? dayjs(restCategories.createdOn) : undefined,
      updatedOn: restCategories.updatedOn ? dayjs(restCategories.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCategories>): HttpResponse<ICategories> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCategories[]>): HttpResponse<ICategories[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
