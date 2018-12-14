import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import MovieSingle from './MovieSingle';

const MoviesView = ({ hasMore = true, loadMore, movies }) => {
	return (
		<div className="wrap movie-view">
			<InfiniteScroll
				pageStart={0}
				loadMore={loadMore}
				hasMore={hasMore}
				loader={
					<div className="d-block text-center" key={-1}>
						Loading ...
					</div>
				}
			>
				<div className="row">
					{movies.map((movie, i) => (
						<MovieSingle
							key={`${i} - ${movie.id}`}
							{...movie}
							isReleased={true}
						/>
					))}
				</div>
			</InfiniteScroll>
		</div>
	);
};

export default MoviesView;
