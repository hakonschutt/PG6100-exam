import React from 'react';
import MoviesView from '../components/layouts/MoviesView';
import MoviesFilter from '../components/layouts/MoviesFilter';

const MoviePage = movie => {
	return (
		<div>
			<div className="container">
				<h1>Movie Page</h1>
				<hr />
				<MoviesFilter />
				<MoviesView />
			</div>
		</div>
	);
};

export default MoviePage;
