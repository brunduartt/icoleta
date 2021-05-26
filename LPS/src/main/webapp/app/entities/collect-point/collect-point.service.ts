import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICollectPoint } from 'app/shared/model/collect-point.model';

type EntityResponseType = HttpResponse<ICollectPoint>;
type EntityArrayResponseType = HttpResponse<ICollectPoint[]>;

@Injectable({ providedIn: 'root' })
export class CollectPointService {
  public resourceUrl = SERVER_API_URL + 'api/collect-points';

  constructor(protected http: HttpClient) {}

  create(collectPoint: ICollectPoint): Observable<EntityResponseType> {
    return this.http.post<ICollectPoint>(this.resourceUrl, collectPoint, { observe: 'response' });
  }

  update(collectPoint: ICollectPoint): Observable<EntityResponseType> {
    return this.http.put<ICollectPoint>(this.resourceUrl, collectPoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICollectPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICollectPoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllFromCurrentUser(): Observable<EntityArrayResponseType> {
    return this.http.get<ICollectPoint[]>(`${this.resourceUrl}/user`, { observe: 'response' });
  }
}
