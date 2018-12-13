import React from 'react';
import Loadable from 'react-loadable';
import PageLoader from '../components/helpers/PageLoader';

export default [
	{
		key: 'home',
		path: '/',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/HomePage'),
			loading: () => {
				return <PageLoader />;
			},
		}),
	},
	{
		key: 'coming',
		path: '/coming',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/ComingMoviesPage'),
			loading: () => {
				return <PageLoader />;
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
				return <PageLoader />;
			},
		}),
	},
	{
		key: 'single-movie',
		path: '/movies/:id',
		exact: true,
		component: Loadable({
			loader: () => import('../pages/MoviePage'),
			loading: () => {
				return <PageLoader />;
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
				return <PageLoader />;
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
				return <PageLoader />;
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
				return <PageLoader />;
			},
		}),
	},
];
