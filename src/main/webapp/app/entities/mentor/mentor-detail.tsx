import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mentor.reducer';

export const MentorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mentorEntity = useAppSelector(state => state.mentor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mentorDetailsHeading">
          <Translate contentKey="apoiaBackOfficeApp.mentor.detail.title">Mentor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="apoiaBackOfficeApp.mentor.id">Id</Translate>
            </span>
          </dt>
          <dd>{mentorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="apoiaBackOfficeApp.mentor.name">Name</Translate>
            </span>
          </dt>
          <dd>{mentorEntity.name}</dd>
          <dt>
            <Translate contentKey="apoiaBackOfficeApp.mentor.areas">Areas</Translate>
          </dt>
          <dd>
            {mentorEntity.areas
              ? mentorEntity.areas.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {mentorEntity.areas && i === mentorEntity.areas.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/mentor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mentor/${mentorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MentorDetail;
