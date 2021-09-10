import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './invoice.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InvoiceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const invoiceEntity = useAppSelector(state => state.invoice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="invoiceDetailsHeading">Invoice</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{invoiceEntity.id}</dd>
          <dt>
            <span id="boltInvoice">Bolt Invoice</span>
          </dt>
          <dd>{invoiceEntity.boltInvoice}</dd>
          <dt>
            <span id="sats">Sats</span>
          </dt>
          <dd>{invoiceEntity.sats}</dd>
          <dt>
            <span id="settled">Settled</span>
          </dt>
          <dd>{invoiceEntity.settled ? 'true' : 'false'}</dd>
          <dt>
            <span id="paidByPubKey">Paid By Pub Key</span>
          </dt>
          <dd>{invoiceEntity.paidByPubKey}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {invoiceEntity.createdAt ? <TextFormat value={invoiceEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="settledAt">Settled At</span>
          </dt>
          <dd>
            {invoiceEntity.settledAt ? <TextFormat value={invoiceEntity.settledAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{invoiceEntity.user ? invoiceEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/invoice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/invoice/${invoiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InvoiceDetail;
