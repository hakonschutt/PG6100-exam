import React from 'react';

import Header from '../components/layouts/Header';
import Footer from '../components/layouts/Footer';

const AppWrapper = ({ children }) => {
	return (
		<div>
			<Header />
			<main style={{ minHeight: 'calc(100vh - 300px)' }}>{children}</main>
			<Footer />
		</div>
	);
};

export default AppWrapper;
