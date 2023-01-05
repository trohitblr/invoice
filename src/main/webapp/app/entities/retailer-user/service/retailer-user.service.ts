import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRetailerUser, NewRetailerUser } from '../retailer-user.model';

export type PartialUpdateRetailerUser = Partial<IRetailerUser> & Pick<IRetailerUser, 'retailerUserId'>;

type RestOf<T extends IRetailerUser | NewRetailerUser> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestRetailerUser = RestOf<IRetailerUser>;

export type NewRestRetailerUser = RestOf<NewRetailerUser>;

export type PartialUpdateRestRetailerUser = RestOf<PartialUpdateRetailerUser>;

export type EntityResponseType = HttpResponse<IRetailerUser>;
export type EntityArrayResponseType = HttpResponse<IRetailerUser[]>;

@Injectable({ providedIn: 'root' })
export class RetailerUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/retailer-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(retailerUser: NewRetailerUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailerUser);
    return this.http
      .post<RestRetailerUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(retailerUser: IRetailerUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailerUser);
    return this.http
      .put<RestRetailerUser>(`${this.resourceUrl}/${this.getRetailerUserIdentifier(retailerUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(retailerUser: PartialUpdateRetailerUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retailerUser);
    return this.http
      .patch<RestRetailerUser>(`${this.resourceUrl}/${this.getRetailerUserIdentifier(retailerUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRetailerUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRetailerUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRetailerUserIdentifier(retailerUser: Pick<IRetailerUser, 'retailerUserId'>): number {
    return retailerUser.retailerUserId;
  }

  compareRetailerUser(o1: Pick<IRetailerUser, 'retailerUserId'> | null, o2: Pick<IRetailerUser, 'retailerUserId'> | null): boolean {
    return o1 && o2 ? this.getRetailerUserIdentifier(o1) === this.getRetailerUserIdentifier(o2) : o1 === o2;
  }

  addRetailerUserToCollectionIfMissing<Type extends Pick<IRetailerUser, 'retailerUserId'>>(
    retailerUserCollection: Type[],
    ...retailerUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const retailerUsers: Type[] = retailerUsersToCheck.filter(isPresent);
    if (retailerUsers.length > 0) {
      const retailerUserCollectionIdentifiers = retailerUserCollection.map(
        retailerUserItem => this.getRetailerUserIdentifier(retailerUserItem)!
      );
      const retailerUsersToAdd = retailerUsers.filter(retailerUserItem => {
        const retailerUserIdentifier = this.getRetailerUserIdentifier(retailerUserItem);
        if (retailerUserCollectionIdentifiers.includes(retailerUserIdentifier)) {
          return false;
        }
        retailerUserCollectionIdentifiers.push(retailerUserIdentifier);
        return true;
      });
      return [...retailerUsersToAdd, ...retailerUserCollection];
    }
    return retailerUserCollection;
  }

  protected convertDateFromClient<T extends IRetailerUser | NewRetailerUser | PartialUpdateRetailerUser>(retailerUser: T): RestOf<T> {
    return {
      ...retailerUser,
      createdOn: retailerUser.createdOn?.toJSON() ?? null,
      updatedOn: retailerUser.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRetailerUser: RestRetailerUser): IRetailerUser {
    return {
      ...restRetailerUser,
      createdOn: restRetailerUser.createdOn ? dayjs(restRetailerUser.createdOn) : undefined,
      updatedOn: restRetailerUser.updatedOn ? dayjs(restRetailerUser.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRetailerUser>): HttpResponse<IRetailerUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRetailerUser[]>): HttpResponse<IRetailerUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
