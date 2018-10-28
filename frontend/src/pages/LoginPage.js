import React, { Component } from 'react';

import requireUnauth from '../hocs/requireUnauth';

class LoginPage extends Component {
	render() {
		return (
			<div>
				<h1>Login page</h1>
			</div>
		);
	}
}

export default requireUnauth(LoginPage);
