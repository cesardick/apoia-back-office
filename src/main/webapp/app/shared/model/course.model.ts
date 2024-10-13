import { IArea } from 'app/shared/model/area.model';

export interface ICourse {
  id?: string;
  title?: string | null;
  description?: string | null;
  seoPublished?: boolean | null;
  area?: IArea | null;
}

export const defaultValue: Readonly<ICourse> = {
  seoPublished: false,
};
