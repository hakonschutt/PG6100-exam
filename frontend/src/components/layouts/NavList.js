import React from 'react';
import { Link } from 'react-router-dom';

const NavList = ({ user }) => {
	const navigation = [];

	navigation.push(
		<li key="movies" className="nav-item">
			<Link className="nav-link text-white" to="/movies">
				Movies
			</Link>
		</li>
	);

	if (user && 'isEnabled' in user && user.isEnabled) {
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

		navigation.push(
			<li key="signup" className="nav-item">
				<Link className="nav-link text-white" to="/signup">
					Sign up
				</Link>
			</li>
		);
	}

	return navigation;
};

export default NavList;
