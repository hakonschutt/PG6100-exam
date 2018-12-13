import React from 'react';

const Jumbotron = ({ children }) => {
	return (
		<div className="jumbotron mb-0">
			<div className="container">{children}</div>
		</div>
	);
};

export default Jumbotron;
