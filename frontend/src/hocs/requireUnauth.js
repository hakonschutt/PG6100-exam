import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function(ComposedComponent) {
	class RequireUnauth extends Component {
		static propTypes = {
			isEnabled: PropTypes.bool.isRequired,
			userType: PropTypes.string.isRequired,
			history: PropTypes.object.isRequired,
		};

		componentDidMount() {
			if (this.props.isEnabled) {
				if (this.props.userType === 'admin') {
					this.props.history.push('/dashboard');
				} else if (this.props.userType === 'user') {
					this.props.history.push('/profile');
				}
			}
		}

		componentWillUpdate(nextProps) {
			if (nextProps.isEnabled) {
				if (nextProps.userType === 'admin') {
					this.props.history.push('/dashboard');
				} else if (nextProps.userType === 'user') {
					this.props.history.push('/profile');
				}
			}
		}

		render() {
			return <ComposedComponent {...this.props} />;
		}
	}

	function mapStateToProps({ auth }) {
		const isEnabled = auth.isEnabled;
		const userType = auth.role;

		return { isEnabled, userType };
	}

	return withRouter(connect(mapStateToProps)(RequireUnauth));
}
