import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import SingleMovie from './SingleMovie';

const MovieList = ({ itemSize, movies: { list } }) => {
	console.log(list);
	return (
		<ul className="movies-list">
			{list.map(movie => {
				console.log(movie);

				return <SingleMovie key={movie.id} itemSize={itemSize} {...movie} />;
			})}
		</ul>
	);
};

function mapStateToProps({ movies }) {
	return { movies };
}

export default connect(mapStateToProps)(MovieList);
