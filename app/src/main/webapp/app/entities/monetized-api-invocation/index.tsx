import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MonetizedApiInvocation from './monetized-api-invocation';
import MonetizedApiInvocationDetail from './monetized-api-invocation-detail';
import MonetizedApiInvocationUpdate from './monetized-api-invocation-update';
import MonetizedApiInvocationDeleteDialog from './monetized-api-invocation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MonetizedApiInvocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MonetizedApiInvocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MonetizedApiInvocationDetail} />
      <ErrorBoundaryRoute path={match.url} component={MonetizedApiInvocation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MonetizedApiInvocationDeleteDialog} />
  </>
);

export default Routes;
