import React from 'react';
import { Route } from 'react-router-dom';

import authRoutes from './authRoutes';

const DashboardRoutes = ({ match }) => {
	return authRoutes.map(route => (
		<Route
			key={route.key}
			exact={route.exact}
			path={`${match.path}${route.path}`}
			component={route.component}
		/>
	));
};

export default DashboardRoutes;
