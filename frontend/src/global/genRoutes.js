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
	{
		key: 'login',
		path: '/login',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/LoginPage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
	{
		key: 'signup',
		path: '/signup',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/SignupPage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
	{
		key: 'profile',
		path: '/profile',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/ProfilePage'),
			loading: () => {
				return <div>Loading...</div>;
			},
		}),
	},
];
