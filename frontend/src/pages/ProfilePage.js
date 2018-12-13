import React from 'react';
import queryString from 'query-string';
import { connect } from 'react-redux';

import Alert from '../components/helpers/Alert';
import Jumbotron from '../components/layouts/Jumbotron';

import requireAuth from '../hocs/requireAuth';

const ProfilePage = ({ auth: { username }, location }) => {
	const parsed = queryString.parse(location.search);

	return (
		<div>
			<Jumbotron>
				<h1 className="display-4">Hei, {username || 'you'}!</h1>
			</Jumbotron>
			<div className="container py-2">
				{parsed.new && <Alert type="success" msg="Welcome as a new member!" />}
				<h2>Purchease history</h2>
				<span>Some list...</span>
			</div>
		</div>
	);
};

function mapStateToProps({ auth }) {
	return { auth };
}

export default connect(mapStateToProps)(requireAuth(ProfilePage));
