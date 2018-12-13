import React from 'react';

const Jumbotron = ({ children }) => {
	return (
		<div className="jumbotron">
			<div className="container">{children}</div>
		</div>
	);
};

export default Jumbotron;
