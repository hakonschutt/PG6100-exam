import React from 'react';
import { Link } from 'react-router-dom';

const NavList = ({ auth }) => {
	const navigation = [];

	navigation.push(
		<li key="movies" id="toMoviesButton" className="nav-item">
			<Link className="nav-link text-white" to="/movies">
				Movies
			</Link>
		</li>
	);

	navigation.push(
		<li key="coming" className="nav-item">
			<Link className="nav-link text-white" to="/coming">
				Coming
			</Link>
		</li>
	);

	if (auth && 'isEnabled' in auth && auth.isEnabled) {
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
