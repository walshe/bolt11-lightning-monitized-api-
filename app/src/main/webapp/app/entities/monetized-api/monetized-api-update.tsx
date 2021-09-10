import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './monetized-api.reducer';
import { IMonetizedApi } from 'app/shared/model/monetized-api.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MonetizedApiUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const monetizedApiEntity = useAppSelector(state => state.monetizedApi.entity);
  const loading = useAppSelector(state => state.monetizedApi.loading);
  const updating = useAppSelector(state => state.monetizedApi.updating);
  const updateSuccess = useAppSelector(state => state.monetizedApi.updateSuccess);

  const handleClose = () => {
    props.history.push('/monetized-api');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...monetizedApiEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...monetizedApiEntity,
          method: 'GET',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monetizedapiApp.monetizedApi.home.createOrEditLabel" data-cy="MonetizedApiCreateUpdateHeading">
            Create or edit a MonetizedApi
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="monetized-api-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Method" id="monetized-api-method" name="method" data-cy="method" type="select">
                <option value="GET">GET</option>
                <option value="POST">POST</option>
                <option value="PUT">PUT</option>
                <option value="PATCH">PATCH</option>
                <option value="DELETE">DELETE</option>
              </ValidatedField>
              <ValidatedField
                label="Uri"
                id="monetized-api-uri"
                name="uri"
                data-cy="uri"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Price In Sats"
                id="monetized-api-priceInSats"
                name="priceInSats"
                data-cy="priceInSats"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/monetized-api" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MonetizedApiUpdate;
