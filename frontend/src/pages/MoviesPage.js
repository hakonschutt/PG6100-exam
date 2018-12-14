import React, { Component } from 'react';
import axios from 'axios';

import Alert from '../components/helpers/Alert';
import MoviesView from '../components/layouts/MoviesView';
import { movieList } from '../actions/data_movies';

class MoviesPage extends Component {
	constructor(props) {
		super(props);

		this.state = {
			movies: movieList,
			nextLink: '/movies-service/api/movies',
			hasMore: false,
			error: '',
		};

		this.loadMore = this.loadMore.bind(this);
	}

	componentDidMount() {
		this.loadMore();
	}

	async loadMore() {
		const { nextLink, movies } = this.state;

		if (!nextLink) {
			this.setState({
				hasMore: false,
			});

			return;
		}

		this.setState({
			movies: [...movies, ...movieList],
			hasMore: true,
		});

		try {
			const res = await axios.get(nextLink);

			const hasMore = res.data._links && res.data._links.next ? true : false;
			const followLink = hasMore ? res.data._links.next : '';

			this.setState({
				hasMore,
				nextLink: followLink,
				movies: [...movies, ...res.data.list],
			});
		} catch (err) {
			this.setState({
				error: 'Could not fetch movies',
				hasMore: false,
			});
		}
	}

	render() {
		const { movies, hasMore, error } = this.state;

		return (
			<div>
				<div className="container">
					<h1 className="d-block mt-3">Movies Page</h1>
					<hr />
					<Alert msg={error} />
					<MoviesView
						hasMore={hasMore}
						loadMore={this.loadMore}
						movies={movies}
					/>
				</div>
			</div>
		);
	}
}

export default MoviesPage;
