import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './monetized-api-invocation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MonetizedApiInvocationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const monetizedApiInvocationEntity = useAppSelector(state => state.monetizedApiInvocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="monetizedApiInvocationDetailsHeading">MonetizedApiInvocation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{monetizedApiInvocationEntity.id}</dd>
          <dt>
            <span id="uri">Uri</span>
          </dt>
          <dd>{monetizedApiInvocationEntity.uri}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {monetizedApiInvocationEntity.createdAt ? (
              <TextFormat value={monetizedApiInvocationEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>User</dt>
          <dd>{monetizedApiInvocationEntity.user ? monetizedApiInvocationEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/monetized-api-invocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/monetized-api-invocation/${monetizedApiInvocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MonetizedApiInvocationDetail;
