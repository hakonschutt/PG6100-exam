import React from 'react';
import MoviesView from '../components/layouts/MoviesView';

const MoviesPage = () => {
	return (
		<div>
			<div className="container">
				<h1 className="d-block mt-3">Movies Page</h1>
				<hr />
				<MoviesView />
			</div>
		</div>
	);
};

export default MoviesPage;
