import React from 'react';
import PropTypes from 'prop-types';
import Loadable from 'react-loadable';

import Header from '../components/layouts/Header';
import Footer from '../components/layouts/Footer';

const LoadableChat = Loadable({
	loader: () => import('../components/chat/Chat'),
	loading: () => {
		return <div id="chat-bubble">...</div>;
	},
});

const AppWrapper = ({ children }) => {
	return (
		<div>
			<Header />
			<main style={{ minHeight: 'calc(100vh - 200px)' }}>{children}</main>
			<Footer />
		</div>
	);
};

AppWrapper.propTypes = {
	children: PropTypes.object.isRequired,
};

export default AppWrapper;
