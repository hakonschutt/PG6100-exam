import React from 'react';
import Loadable from 'react-loadable';

export default [
	{
		key: 'comp1',
		path: '/',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/HomePage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
];
