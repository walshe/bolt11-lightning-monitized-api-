import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Invoice from './invoice';
import Balance from './balance';
import MonetizedApi from './monetized-api';
import MonetizedApiInvocation from './monetized-api-invocation';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}invoice`} component={Invoice} />
      <ErrorBoundaryRoute path={`${match.url}balance`} component={Balance} />
      <ErrorBoundaryRoute path={`${match.url}monetized-api`} component={MonetizedApi} />
      <ErrorBoundaryRoute path={`${match.url}monetized-api-invocation`} component={MonetizedApiInvocation} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
