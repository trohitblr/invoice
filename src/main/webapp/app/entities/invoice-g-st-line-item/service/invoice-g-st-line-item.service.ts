import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceGStLineItem, NewInvoiceGStLineItem } from '../invoice-g-st-line-item.model';

export type PartialUpdateInvoiceGStLineItem = Partial<IInvoiceGStLineItem> & Pick<IInvoiceGStLineItem, 'invoiceGStLineItemId'>;

type RestOf<T extends IInvoiceGStLineItem | NewInvoiceGStLineItem> = Omit<T, 'createdOn' | 'updatedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
};

export type RestInvoiceGStLineItem = RestOf<IInvoiceGStLineItem>;

export type NewRestInvoiceGStLineItem = RestOf<NewInvoiceGStLineItem>;

export type PartialUpdateRestInvoiceGStLineItem = RestOf<PartialUpdateInvoiceGStLineItem>;

export type EntityResponseType = HttpResponse<IInvoiceGStLineItem>;
export type EntityArrayResponseType = HttpResponse<IInvoiceGStLineItem[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceGStLineItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-g-st-line-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceGStLineItem: NewInvoiceGStLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceGStLineItem);
    return this.http
      .post<RestInvoiceGStLineItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(invoiceGStLineItem: IInvoiceGStLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceGStLineItem);
    return this.http
      .put<RestInvoiceGStLineItem>(`${this.resourceUrl}/${this.getInvoiceGStLineItemIdentifier(invoiceGStLineItem)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(invoiceGStLineItem: PartialUpdateInvoiceGStLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceGStLineItem);
    return this.http
      .patch<RestInvoiceGStLineItem>(`${this.resourceUrl}/${this.getInvoiceGStLineItemIdentifier(invoiceGStLineItem)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInvoiceGStLineItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInvoiceGStLineItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInvoiceGStLineItemIdentifier(invoiceGStLineItem: Pick<IInvoiceGStLineItem, 'invoiceGStLineItemId'>): number {
    return invoiceGStLineItem.invoiceGStLineItemId;
  }

  compareInvoiceGStLineItem(
    o1: Pick<IInvoiceGStLineItem, 'invoiceGStLineItemId'> | null,
    o2: Pick<IInvoiceGStLineItem, 'invoiceGStLineItemId'> | null
  ): boolean {
    return o1 && o2 ? this.getInvoiceGStLineItemIdentifier(o1) === this.getInvoiceGStLineItemIdentifier(o2) : o1 === o2;
  }

  addInvoiceGStLineItemToCollectionIfMissing<Type extends Pick<IInvoiceGStLineItem, 'invoiceGStLineItemId'>>(
    invoiceGStLineItemCollection: Type[],
    ...invoiceGStLineItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceGStLineItems: Type[] = invoiceGStLineItemsToCheck.filter(isPresent);
    if (invoiceGStLineItems.length > 0) {
      const invoiceGStLineItemCollectionIdentifiers = invoiceGStLineItemCollection.map(
        invoiceGStLineItemItem => this.getInvoiceGStLineItemIdentifier(invoiceGStLineItemItem)!
      );
      const invoiceGStLineItemsToAdd = invoiceGStLineItems.filter(invoiceGStLineItemItem => {
        const invoiceGStLineItemIdentifier = this.getInvoiceGStLineItemIdentifier(invoiceGStLineItemItem);
        if (invoiceGStLineItemCollectionIdentifiers.includes(invoiceGStLineItemIdentifier)) {
          return false;
        }
        invoiceGStLineItemCollectionIdentifiers.push(invoiceGStLineItemIdentifier);
        return true;
      });
      return [...invoiceGStLineItemsToAdd, ...invoiceGStLineItemCollection];
    }
    return invoiceGStLineItemCollection;
  }

  protected convertDateFromClient<T extends IInvoiceGStLineItem | NewInvoiceGStLineItem | PartialUpdateInvoiceGStLineItem>(
    invoiceGStLineItem: T
  ): RestOf<T> {
    return {
      ...invoiceGStLineItem,
      createdOn: invoiceGStLineItem.createdOn?.toJSON() ?? null,
      updatedOn: invoiceGStLineItem.updatedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInvoiceGStLineItem: RestInvoiceGStLineItem): IInvoiceGStLineItem {
    return {
      ...restInvoiceGStLineItem,
      createdOn: restInvoiceGStLineItem.createdOn ? dayjs(restInvoiceGStLineItem.createdOn) : undefined,
      updatedOn: restInvoiceGStLineItem.updatedOn ? dayjs(restInvoiceGStLineItem.updatedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInvoiceGStLineItem>): HttpResponse<IInvoiceGStLineItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInvoiceGStLineItem[]>): HttpResponse<IInvoiceGStLineItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
