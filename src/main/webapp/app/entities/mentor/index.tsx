import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Mentor from './mentor';
import MentorDetail from './mentor-detail';
import MentorUpdate from './mentor-update';
import MentorDeleteDialog from './mentor-delete-dialog';

const MentorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Mentor />} />
    <Route path="new" element={<MentorUpdate />} />
    <Route path=":id">
      <Route index element={<MentorDetail />} />
      <Route path="edit" element={<MentorUpdate />} />
      <Route path="delete" element={<MentorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MentorRoutes;
