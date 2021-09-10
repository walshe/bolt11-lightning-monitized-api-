import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './monetized-api-invocation.reducer';

export const MonetizedApiInvocationDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const monetizedApiInvocationEntity = useAppSelector(state => state.monetizedApiInvocation.entity);
  const updateSuccess = useAppSelector(state => state.monetizedApiInvocation.updateSuccess);

  const handleClose = () => {
    props.history.push('/monetized-api-invocation');
  };

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(monetizedApiInvocationEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="monetizedApiInvocationDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="monetizedapiApp.monetizedApiInvocation.delete.question">
        Are you sure you want to delete this MonetizedApiInvocation?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-monetizedApiInvocation" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default MonetizedApiInvocationDeleteDialog;
