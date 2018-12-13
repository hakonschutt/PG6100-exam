import React from 'react';
import MoviesView from '../components/layouts/MoviesView';

const ComingMoviesPage = () => {
	return (
		<div>
			<div className="container">
				<h1 className="d-block mt-3">Movies Coming Soon</h1>
				<hr />
				<MoviesView />
			</div>
		</div>
	);
};

export default ComingMoviesPage;
