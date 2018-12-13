import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import Logo from '../../assets/imgs/logo_white.svg';
import NavList from './NavList';

class Header extends Component {
	render() {
		return (
			<nav className="navbar navbar-expand-lg navbar-light bg-dark">
				<div className="container">
					<Link to="/" className="navbar-brand" href="#">
						<img
							src={Logo}
							className="d-inline-block align-top"
							alt="house of movies logo with popcorn icon"
							style={{ height: '30px' }}
						/>
					</Link>
					<div className="collapse navbar-collapse">
						<ul className="navbar-nav mr-auto float-right">
							<NavList user={this.props.user} />
						</ul>
					</div>
				</div>
			</nav>
		);
	}
}

Header.propTypes = {
	user: PropTypes.object,
};

function mapStateToProps({ user }) {
	return { user };
}

export default connect(
	null,
	mapStateToProps
)(Header);
