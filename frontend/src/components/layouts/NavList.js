import React from 'react';
import { Link } from 'react-router-dom';

const NavList = ({ user }) => {
	const navigation = [];

	navigation.push(
		<li key="movies">
			<Link to="/">Movies</Link>
		</li>
	);

	if (user && 'enabled' in user && user.enabled) {
		navigation.push(
			<li key="profile">
				<Link to="/profile">Profile</Link>
			</li>
		);
	} else {
		navigation.push(
			<li key="login">
				<Link to="/login">Login</Link>
			</li>
		);
	}

	if (
		user &&
		'enabled' in user &&
		user.enabled &&
		'role' in user &&
		user.role === 3
	) {
		navigation.push(
			<li key="dashboard">
				<Link to="/dashboard">Dashboard</Link>
			</li>
		);
	}

	return navigation;
};

export default NavList;
