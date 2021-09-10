import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IBalance {
  id?: number;
  sats?: number;
  updatedAt?: string | null;
  user?: IUser;
}

export const defaultValue: Readonly<IBalance> = {};
