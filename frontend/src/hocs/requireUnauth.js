import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

export default function(ComposedComponent) {
	class RequireUnauth extends Component {
		componentDidMount() {
			if (this.props.isEnabled) {
				this.props.history.push('/profile');
			}
		}

		componentWillUpdate(nextProps) {
			if (nextProps.isEnabled) {
				this.props.history.push('/profile');
			}
		}

		render() {
			return <ComposedComponent {...this.props} />;
		}
	}

	function mapStateToProps({ auth }) {
		return { isEnabled: auth.isEnabled };
	}

	return withRouter(connect(mapStateToProps)(RequireUnauth));
}
