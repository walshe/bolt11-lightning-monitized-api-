import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MonetizedApi from './monetized-api';
import MonetizedApiDetail from './monetized-api-detail';
import MonetizedApiUpdate from './monetized-api-update';
import MonetizedApiDeleteDialog from './monetized-api-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MonetizedApiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MonetizedApiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MonetizedApiDetail} />
      <ErrorBoundaryRoute path={match.url} component={MonetizedApi} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MonetizedApiDeleteDialog} />
  </>
);

export default Routes;
