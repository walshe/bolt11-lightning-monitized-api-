import { Method } from 'app/shared/model/enumerations/method.model';

export interface IMonetizedApi {
  id?: number;
  method?: Method;
  uri?: string;
  priceInSats?: number;
}

export const defaultValue: Readonly<IMonetizedApi> = {};
