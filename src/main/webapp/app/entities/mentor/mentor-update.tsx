import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAreas } from 'app/entities/area/area.reducer';
import { createEntity, getEntity, reset, updateEntity } from './mentor.reducer';

export const MentorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const areas = useAppSelector(state => state.area.entities);
  const mentorEntity = useAppSelector(state => state.mentor.entity);
  const loading = useAppSelector(state => state.mentor.loading);
  const updating = useAppSelector(state => state.mentor.updating);
  const updateSuccess = useAppSelector(state => state.mentor.updateSuccess);

  const handleClose = () => {
    navigate(`/mentor${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAreas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...mentorEntity,
      ...values,
      areas: mapIdList(values.areas),
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
          ...mentorEntity,
          areas: mentorEntity?.areas?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="apoiaBackOfficeApp.mentor.home.createOrEditLabel" data-cy="MentorCreateUpdateHeading">
            <Translate contentKey="apoiaBackOfficeApp.mentor.home.createOrEditLabel">Create or edit a Mentor</Translate>
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
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="mentor-id"
                  label={translate('apoiaBackOfficeApp.mentor.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('apoiaBackOfficeApp.mentor.name')} id="mentor-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('apoiaBackOfficeApp.mentor.areas')}
                id="mentor-areas"
                data-cy="areas"
                type="select"
                multiple
                name="areas"
              >
                <option value="" key="0" />
                {areas
                  ? areas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mentor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MentorUpdate;
