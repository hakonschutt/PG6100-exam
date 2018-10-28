import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function(ComposedComponent) {
	class RequireAdmin extends Component {
		static propTypes = {
			isEnabled: PropTypes.bool.isRequired,
			userType: PropTypes.string.isRequired,
			history: PropTypes.object.isRequired,
		};

		componentDidMount() {
			if (!this.props.isEnabled) {
				this.props.history.push('/404');
			} else if (this.props.userType === 'user') {
				this.props.history.push('/404');
			}
		}

		componentWillUpdate(nextProps) {
			if (!nextProps.isEnabled) {
				this.props.history.push('/404');
			} else if (nextProps.userType === 'user') {
				this.props.history.push('/404');
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

	return withRouter(connect(mapStateToProps)(RequireAdmin));
}
