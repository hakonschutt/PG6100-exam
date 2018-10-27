import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import InfiniteScroll from 'react-infinite-scroller';

import SingleMovie from './SingleMovie';
import { fetchMovies } from '../../actions';

class MoviesView extends Component {
	componentDidMount() {
		this.props.fetchMovies();
	}

	render() {
		let i = 0;

		return (
			<div className="wrap movie-view">
				<InfiniteScroll
					pageStart={0}
					loadMore={this.props.fetchMovies}
					hasMore={true}
					loader={
						<div className="loader" key={0}>
							Loading ...
						</div>
					}
				>
					<div className="movie-view-inner">
						<ul>
							{this.props.movies.list.map(movie => {
								return <SingleMovie key={`${++i} - ${movie.id}`} {...movie} />;
							})}
						</ul>
					</div>
				</InfiniteScroll>
			</div>
		);
	}
}

function mapStateToProps({ movies }) {
	return { movies };
}

export default connect(
	mapStateToProps,
	{ fetchMovies }
)(MoviesView);
