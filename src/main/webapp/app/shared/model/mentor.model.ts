import { IArea } from 'app/shared/model/area.model';

export interface IMentor {
  id?: string;
  name?: string | null;
  areas?: IArea[] | null;
}

export const defaultValue: Readonly<IMentor> = {};
