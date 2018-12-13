import React from 'react';
import { Link } from 'react-router-dom';

const NotFound = () => {
	return (
		<div
			className="page-wrap d-flex flex-row align-items-center"
			style={{ minHeight: 'calc(100vh - 400px)' }}
		>
			<div className="container">
				<div className="row justify-content-center">
					<div className="col-md-12 text-center">
						<span className="display-1 d-block">404</span>
						<div className="mb-4 lead">
							{'We can\'t seem to find the page you\'re looking for.'}
						</div>
						<Link to="/" className="btn btn-link">
							Back to Home
						</Link>
					</div>
				</div>
			</div>
		</div>
	);
};

export default NotFound;
