import React from 'react';
import { connect } from 'react-redux';

import Jumbotron from '../components/layouts/Jumbotron';

//import requireAuth from '../hocs/requireAuth';

const ProfilePage = ({ auth: { username } }) => {
	console.log(username);
	return (
		<div>
			<Jumbotron>
				<h1 className="display-4">Hei, {username || 'you'}!</h1>
			</Jumbotron>
			<div className="container">
				<h2>Purchease history</h2>
				<span>Some list...</span>
			</div>
		</div>
	);
};

function mapStateToProps({ auth }) {
	return { auth };
}

export default connect(mapStateToProps)(ProfilePage);
