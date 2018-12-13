import React from 'react';
import { Link } from 'react-router-dom';

const NavList = ({ user }) => {
	const navigation = [];

	navigation.push(
		<li key="movies" className="nav-item">
			<Link className="nav-link text-white" to="/">
				Movies
			</Link>
		</li>
	);

	if (user && 'enabled' in user && user.enabled) {
		navigation.push(
			<li key="profile" className="nav-item">
				<Link className="nav-link text-white" to="/profile">
					Profile
				</Link>
			</li>
		);
	} else {
		navigation.push(
			<li key="login" className="nav-item">
				<Link className="nav-link text-white" to="/login">
					Login
				</Link>
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
			<li key="dashboard" className="nav-item">
				<Link className="nav-link text-white" to="/dashboard">
					Dashboard
				</Link>
			</li>
		);
	}

	return navigation;
};

export default NavList;
