import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IMonetizedApiInvocation {
  id?: number;
  uri?: string;
  createdAt?: string;
  user?: IUser;
}

export const defaultValue: Readonly<IMonetizedApiInvocation> = {};
