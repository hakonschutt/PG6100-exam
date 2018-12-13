import React from 'react';

const Alert = ({ type, msg }) => {
	if (!msg) return null;

	return (
		<div className={`alert alert-${type}`} role="alert">
			{msg}
		</div>
	);
};

export default Alert;
