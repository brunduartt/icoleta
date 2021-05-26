import { IMaterial } from 'app/shared/model/material.model';
import { IUser } from 'app/core/user/user.model';

export interface ICollectPoint {
  id?: number;
  name?: string;
  description?: string;
  lat?: number;
  lon?: number;
  materials?: IMaterial[];
  users?: IUser[];
  createdByLogin?: string;
  createdById?: number;
}

export class CollectPoint implements ICollectPoint {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public lat?: number,
    public lon?: number,
    public materials?: IMaterial[],
    public users?: IUser[],
    public createdByLogin?: string,
    public createdById?: number
  ) {}
}
