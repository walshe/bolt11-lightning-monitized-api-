import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IInvoice {
  id?: number;
  boltInvoice?: string;
  sats?: number;
  settled?: boolean;
  paidByPubKey?: string | null;
  createdAt?: string;
  settledAt?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IInvoice> = {
  settled: false,
};
