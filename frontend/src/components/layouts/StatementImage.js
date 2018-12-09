import React from 'react';

const StatementImage = ({ img, alt, side, children }) => {
	return (
		<div className="statement-image">
			<img className="image" src={img} alt={alt} />
			<div className="overlay" />
			<div className="wrap clearfix hpad">
				<div className={`statement-image-inner ${side}`}>{children}</div>
			</div>
		</div>
	);
};

export default StatementImage;
