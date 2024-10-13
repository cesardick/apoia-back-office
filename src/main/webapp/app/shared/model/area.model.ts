import { IMentor } from 'app/shared/model/mentor.model';

export interface IArea {
  id?: string;
  name?: string | null;
  mentors?: IMentor[] | null;
}

export const defaultValue: Readonly<IArea> = {};
