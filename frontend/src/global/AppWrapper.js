import React from 'react';
import PropTypes from 'prop-types';

import Header from '../components/layouts/Header';
import Footer from '../components/layouts/Footer';

const AppWrapper = ({ children }) => {
	// ADD GLOBALE COMPONENTS

	console.log('CHILDREN', children);

	return (
		<div>
			<Header />
			<main>{children}</main>
			<Footer />
		</div>
	);
};

AppWrapper.propTypes = {
	children: PropTypes.object.isRequired,
};

export default AppWrapper;
