import React from 'react';
import PropTypes from 'prop-types';

const AppWrapper = ({ children }) => {
	// ADD GLOBALE COMPONENTS

	console.log('CHILDREN', children);

	return (
		<div>
			<h1>Dette er e</h1>
			<main>{children}</main>
		</div>
	);
};

AppWrapper.propTypes = {
	children: PropTypes.object.isRequired,
};

export default AppWrapper;
