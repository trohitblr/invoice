import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceLineItem, NewInvoiceLineItem } from '../invoice-line-item.model';

export type PartialUpdateInvoiceLineItem = Partial<IInvoiceLineItem> & Pick<IInvoiceLineItem, 'invoiceLineItemId'>;

type RestOf<T extends IInvoiceLineItem | NewInvoiceLineItem> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestInvoiceLineItem = RestOf<IInvoiceLineItem>;

export type NewRestInvoiceLineItem = RestOf<NewInvoiceLineItem>;

export type PartialUpdateRestInvoiceLineItem = RestOf<PartialUpdateInvoiceLineItem>;

export type EntityResponseType = HttpResponse<IInvoiceLineItem>;
export type EntityArrayResponseType = HttpResponse<IInvoiceLineItem[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-line-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceLineItem: NewInvoiceLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceLineItem);
    return this.http
      .post<RestInvoiceLineItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(invoiceLineItem: IInvoiceLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceLineItem);
    return this.http
      .put<RestInvoiceLineItem>(`${this.resourceUrl}/${this.getInvoiceLineItemIdentifier(invoiceLineItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(invoiceLineItem: PartialUpdateInvoiceLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceLineItem);
    return this.http
      .patch<RestInvoiceLineItem>(`${this.resourceUrl}/${this.getInvoiceLineItemIdentifier(invoiceLineItem)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInvoiceLineItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInvoiceLineItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInvoiceLineItemIdentifier(invoiceLineItem: Pick<IInvoiceLineItem, 'invoiceLineItemId'>): number {
    return invoiceLineItem.invoiceLineItemId;
  }

  compareInvoiceLineItem(
    o1: Pick<IInvoiceLineItem, 'invoiceLineItemId'> | null,
    o2: Pick<IInvoiceLineItem, 'invoiceLineItemId'> | null
  ): boolean {
    return o1 && o2 ? this.getInvoiceLineItemIdentifier(o1) === this.getInvoiceLineItemIdentifier(o2) : o1 === o2;
  }

  addInvoiceLineItemToCollectionIfMissing<Type extends Pick<IInvoiceLineItem, 'invoiceLineItemId'>>(
    invoiceLineItemCollection: Type[],
    ...invoiceLineItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceLineItems: Type[] = invoiceLineItemsToCheck.filter(isPresent);
    if (invoiceLineItems.length > 0) {
      const invoiceLineItemCollectionIdentifiers = invoiceLineItemCollection.map(
        invoiceLineItemItem => this.getInvoiceLineItemIdentifier(invoiceLineItemItem)!
      );
      const invoiceLineItemsToAdd = invoiceLineItems.filter(invoiceLineItemItem => {
        const invoiceLineItemIdentifier = this.getInvoiceLineItemIdentifier(invoiceLineItemItem);
        if (invoiceLineItemCollectionIdentifiers.includes(invoiceLineItemIdentifier)) {
          return false;
        }
        invoiceLineItemCollectionIdentifiers.push(invoiceLineItemIdentifier);
        return true;
      });
      return [...invoiceLineItemsToAdd, ...invoiceLineItemCollection];
    }
    return invoiceLineItemCollection;
  }

  protected convertDateFromClient<T extends IInvoiceLineItem | NewInvoiceLineItem | PartialUpdateInvoiceLineItem>(
    invoiceLineItem: T
  ): RestOf<T> {
    return {
      ...invoiceLineItem,
      createdOn: invoiceLineItem.createdOn?.toJSON() ?? null,
      updatedOn: invoiceLineItem.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInvoiceLineItem: RestInvoiceLineItem): IInvoiceLineItem {
    return {
      ...restInvoiceLineItem,
      createdOn: restInvoiceLineItem.createdOn ? dayjs(restInvoiceLineItem.createdOn) : undefined,
      updatedOn: restInvoiceLineItem.updatedOn ? dayjs(restInvoiceLineItem.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInvoiceLineItem>): HttpResponse<IInvoiceLineItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInvoiceLineItem[]>): HttpResponse<IInvoiceLineItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
