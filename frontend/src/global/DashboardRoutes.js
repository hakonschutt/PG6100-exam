import React from 'react';
import { Route } from 'react-router-dom';

import authRoutesJson from './authRoutesJson';

const DashboardRoutes = ({ match }) => {
	return authRoutesJson.map(route => (
		<Route
			key={route.key}
			exact={route.exact}
			path={`${match.path}${route.path}`}
			component={route.component}
		/>
	));
};

export default DashboardRoutes;
