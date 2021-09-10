import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './monetized-api.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MonetizedApiDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const monetizedApiEntity = useAppSelector(state => state.monetizedApi.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="monetizedApiDetailsHeading">MonetizedApi</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{monetizedApiEntity.id}</dd>
          <dt>
            <span id="method">Method</span>
          </dt>
          <dd>{monetizedApiEntity.method}</dd>
          <dt>
            <span id="uri">Uri</span>
          </dt>
          <dd>{monetizedApiEntity.uri}</dd>
          <dt>
            <span id="priceInSats">Price In Sats</span>
          </dt>
          <dd>{monetizedApiEntity.priceInSats}</dd>
        </dl>
        <Button tag={Link} to="/monetized-api" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/monetized-api/${monetizedApiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MonetizedApiDetail;
