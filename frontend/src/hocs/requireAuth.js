import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function(ComposedComponent) {
	class RequireAuth extends Component {
		static propTypes = {
			isEnabled: PropTypes.bool.isRequired,
			history: PropTypes.object.isRequired,
		};

		componentDidMount() {
			if (!this.props.isEnabled) {
				this.props.history.push('/login');
			}
		}

		componentWillUpdate(nextProps) {
			if (!nextProps.isEnabled) {
				this.props.history.push('/login');
			}
		}

		render() {
			return <ComposedComponent {...this.props} />;
		}
	}

	function mapStateToProps({ auth }) {
		const isEnabled = auth && 'isEnabled' in auth && auth.isEnabled;

		return { isEnabled };
	}

	return withRouter(connect(mapStateToProps)(RequireAuth));
}
