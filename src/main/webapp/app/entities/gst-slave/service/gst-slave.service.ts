import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGstSlave, NewGstSlave } from '../gst-slave.model';

export type PartialUpdateGstSlave = Partial<IGstSlave> & Pick<IGstSlave, 'gstSlaveId'>;

export type EntityResponseType = HttpResponse<IGstSlave>;
export type EntityArrayResponseType = HttpResponse<IGstSlave[]>;

@Injectable({ providedIn: 'root' })
export class GstSlaveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gst-slaves');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gstSlave: NewGstSlave): Observable<EntityResponseType> {
    return this.http.post<IGstSlave>(this.resourceUrl, gstSlave, { observe: 'response' });
  }

  update(gstSlave: IGstSlave): Observable<EntityResponseType> {
    return this.http.put<IGstSlave>(`${this.resourceUrl}/${this.getGstSlaveIdentifier(gstSlave)}`, gstSlave, { observe: 'response' });
  }

  partialUpdate(gstSlave: PartialUpdateGstSlave): Observable<EntityResponseType> {
    return this.http.patch<IGstSlave>(`${this.resourceUrl}/${this.getGstSlaveIdentifier(gstSlave)}`, gstSlave, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGstSlave>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGstSlave[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGstSlaveIdentifier(gstSlave: Pick<IGstSlave, 'gstSlaveId'>): number {
    return gstSlave.gstSlaveId;
  }

  compareGstSlave(o1: Pick<IGstSlave, 'gstSlaveId'> | null, o2: Pick<IGstSlave, 'gstSlaveId'> | null): boolean {
    return o1 && o2 ? this.getGstSlaveIdentifier(o1) === this.getGstSlaveIdentifier(o2) : o1 === o2;
  }

  addGstSlaveToCollectionIfMissing<Type extends Pick<IGstSlave, 'gstSlaveId'>>(
    gstSlaveCollection: Type[],
    ...gstSlavesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gstSlaves: Type[] = gstSlavesToCheck.filter(isPresent);
    if (gstSlaves.length > 0) {
      const gstSlaveCollectionIdentifiers = gstSlaveCollection.map(gstSlaveItem => this.getGstSlaveIdentifier(gstSlaveItem)!);
      const gstSlavesToAdd = gstSlaves.filter(gstSlaveItem => {
        const gstSlaveIdentifier = this.getGstSlaveIdentifier(gstSlaveItem);
        if (gstSlaveCollectionIdentifiers.includes(gstSlaveIdentifier)) {
          return false;
        }
        gstSlaveCollectionIdentifiers.push(gstSlaveIdentifier);
        return true;
      });
      return [...gstSlavesToAdd, ...gstSlaveCollection];
    }
    return gstSlaveCollection;
  }
}
