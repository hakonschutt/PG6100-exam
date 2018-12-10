import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import Logo from '../../assets/imgs/logo_white.svg';
import NavList from './NavList';

class Header extends Component {
	render() {
		return (
			<header>
				<div className="wrap clearfix">
					<div className="logo-wrap">
						<Link to="/">
							<img src={Logo} alt="house of movies logo with popcorn icon" />
						</Link>
					</div>
					<nav role="navigation">
						<ul>
							<NavList user={this.props.user} />
						</ul>
					</nav>
				</div>
			</header>
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
