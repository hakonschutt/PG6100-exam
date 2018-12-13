import React, { Component } from 'react';
import InfiniteScroll from 'react-infinite-scroller';

import MovieSingle from './MovieSingle';

class MoviesView extends Component {
	render() {
		const movies = { list: [] };

		return (
			<div className="wrap movie-view">
				<InfiniteScroll
					pageStart={0}
					loadMore={() => console.log('Hello')}
					hasMore={true}
					loader={
						<div className="loader" key={0}>
							Loading ...
						</div>
					}
				>
					<div className="movie-view-inner">
						<ul>
							{movies.list.map((movie, i) => (
								<MovieSingle key={`${i} - ${movie.id}`} {...movie} />
							))}
						</ul>
					</div>
				</InfiniteScroll>
			</div>
		);
	}
}

export default MoviesView;
