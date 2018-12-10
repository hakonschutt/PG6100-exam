import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function(ComposedComponent) {
	class RequireAuth extends Component {
		static propTypes = {
			isEnabled: PropTypes.bool.isRequired,
			userType: PropTypes.string.isRequired,
			history: PropTypes.object.isRequired,
		};

		componentDidMount() {
			if (!this.props.isEnabled) {
				this.props.history.push('/login');
			} else if (this.props.userType === 'admin') {
				this.props.history.push('/dashboard/settings');
			}
		}

		componentWillUpdate(nextProps) {
			if (!nextProps.isEnabled) {
				this.props.history.push('/login');
			} else if (nextProps.userType === 'admin') {
				this.props.history.push('/dashboard/settings');
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

	return withRouter(connect(mapStateToProps)(RequireAuth));
}
