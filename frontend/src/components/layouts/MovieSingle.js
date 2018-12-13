import React from 'react';

const MovieSingle = ({ poster, title, itemSize = 200 }) => {
	return (
		<li className="single-movie">
			<a onClick={console.log} href="#0">
				<img
					src={`https://image.tmdb.org/t/p/w200${poster}`}
					alt={`${title} cover art`}
				/>
			</a>
		</li>
	);
};

export default MovieSingle;
