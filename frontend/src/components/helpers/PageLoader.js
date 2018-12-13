import React from 'react';

const PageLoader = () => {
	return (
		<div
			className="page-wrap d-flex flex-row align-items-center"
			style={{ minHeight: 'calc(100vh - 400px)' }}
		>
			<div className="container">
				<div className="row justify-content-center">
					<div className="col-md-12 text-center">
						<span className="display-3 d-block">Loading...</span>
					</div>
				</div>
			</div>
		</div>
	);
};

export default PageLoader;
