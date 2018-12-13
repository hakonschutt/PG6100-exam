import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { withRouter } from 'react-router-dom';

import { signoutUser } from '../../actions';
import Logo from '../../assets/imgs/logo_white.svg';
import NavList from './NavList';

class Header extends Component {
	constructor(props) {
		super(props);

		this.logout = this.logout.bind(this);
	}

	logout() {
		const { signoutUser, history } = this.props;

		signoutUser((gotError, msg) => {
			if (!gotError) {
				history.push('/');
			}
		});
	}

	render() {
		const { auth } = this.props;

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
							<NavList auth={auth} />
						</ul>
						{auth &&
							auth.isEnabled && (
							<a
								onClick={this.logout}
								href="#0"
								className="btn btn-light my-2 my-sm-0"
							>
									Logout
							</a>
						)}
					</div>
				</div>
			</nav>
		);
	}
}

function mapStateToProps({ auth }) {
	return { auth };
}

export default connect(
	mapStateToProps,
	{ signoutUser }
)(withRouter(Header));
