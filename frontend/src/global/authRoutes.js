import React from 'react';
import Loadable from 'react-loadable';

export default [
	{
		key: 'dashboard',
		path: '/',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/dashboard/DashboardPage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
];
