import { ICollectPoint } from 'app/shared/model/collect-point.model';
import { MaterialType } from 'app/shared/model/enumerations/material-type.model';

export interface IMaterial {
  id?: number;
  name?: string;
  materialType?: MaterialType;
  collectPoints?: ICollectPoint[];
}

export class Material implements IMaterial {
  constructor(public id?: number, public name?: string, public materialType?: MaterialType, public collectPoints?: ICollectPoint[]) {}
}
