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
	{
		key: 'dashboard/movies',
		path: '/movies',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/dashboard/DashboardMoviesPage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
	{
		key: 'dashboard/events',
		path: '/events',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/dashboard/DashboardEventsPage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
];
