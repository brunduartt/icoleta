import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMaterial } from 'app/shared/model/material.model';

type EntityResponseType = HttpResponse<IMaterial>;
type EntityArrayResponseType = HttpResponse<IMaterial[]>;

@Injectable({ providedIn: 'root' })
export class MaterialService {
  public resourceUrl = SERVER_API_URL + 'api/materials';

  constructor(protected http: HttpClient) {}

  create(material: IMaterial): Observable<EntityResponseType> {
    return this.http.post<IMaterial>(this.resourceUrl, material, { observe: 'response' });
  }

  update(material: IMaterial): Observable<EntityResponseType> {
    return this.http.put<IMaterial>(this.resourceUrl, material, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
