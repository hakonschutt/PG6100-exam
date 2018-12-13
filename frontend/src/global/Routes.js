import React, { Component } from 'react';
import {
	BrowserRouter as Router,
	Route,
	Switch,
	Redirect,
} from 'react-router-dom';
import { connect } from 'react-redux';

import { fetchUser } from '../actions';
import AppWrapper from './AppWrapper';
import NotFoundPage from '../pages/NotFoundPage';
import DashboardRoutes from './DashboardRoutes';
import genRoutes from './genRoutes';
import requireAdmin from '../hocs/requireAdmin';
import LoginPage from "../pages/LoginPage";

class Routes extends Component {
	componentDidMount() {
		this.props.fetchUser();
	}

	render() {
		return (
			<Router>
				<AppWrapper>
					<Switch>
						{genRoutes.map(route => (
							<Route
								exact={route.exact}
								key={route.key}
								path={route.path}
								component={route.component}
							/>
						))}
						<Route
							path="/dashboard"
							component={requireAdmin(DashboardRoutes)}
						/>
						<Route path="/login" component={LoginPage} />
						<Route path="/not" component={NotFoundPage} />
						<Route path="/404" component={NotFoundPage} />
						<Redirect to="/404" />
					</Switch>
				</AppWrapper>
			</Router>
		);
	}
}

export default connect(
	null,
	{ fetchUser }
)(Routes);
