import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './area.reducer';

export const AreaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const areaEntity = useAppSelector(state => state.area.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="areaDetailsHeading">
          <Translate contentKey="apoiaBackOfficeApp.area.detail.title">Area</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="apoiaBackOfficeApp.area.id">Id</Translate>
            </span>
          </dt>
          <dd>{areaEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="apoiaBackOfficeApp.area.name">Name</Translate>
            </span>
          </dt>
          <dd>{areaEntity.name}</dd>
          <dt>
            <Translate contentKey="apoiaBackOfficeApp.area.mentors">Mentors</Translate>
          </dt>
          <dd>
            {areaEntity.mentors
              ? areaEntity.mentors.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {areaEntity.mentors && i === areaEntity.mentors.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/area" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/area/${areaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AreaDetail;
