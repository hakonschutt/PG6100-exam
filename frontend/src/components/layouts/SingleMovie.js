import React from 'react';

const SingleMovie = ({ poster, title, itemSize }) => {
	return (
		<li className="single-movie" style={{ width: `200px` }}>
			<a onClick={console.log} href="#0">
				<img
					src={`https://image.tmdb.org/t/p/w200${poster}`}
					alt={`${title} cover art`}
				/>
			</a>
		</li>
	);
};

export default SingleMovie;
