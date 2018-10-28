import React from 'react';
import Loadable from 'react-loadable';

export default [
	{
		key: 'home',
		path: '/',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/HomePage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
	{
		key: 'movies',
		path: '/movies',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/MoviesPage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
];
