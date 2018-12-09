import React from 'react';

import requireAuth from '../hocs/requireAuth';

const ProfilePage = () => {
	return (
		<div>
			<h1>Profile Page</h1>
		</div>
	);
};

export default requireAuth(ProfilePage);
