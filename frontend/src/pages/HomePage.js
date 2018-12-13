import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import Jumbotron from '../components/layouts/Jumbotron';
import MoviesView from '../components/layouts/MoviesView';
import MoviesFilter from '../components/layouts/MoviesFilter';

class HomePage extends Component {
	render() {
		return (
			<div>
				<Jumbotron>
					<h1 className="display-4">Deadpool 2!</h1>
					<p className="lead">
						Lorem Ipsum has been the industry's standard dummy text ever since
						the 1500s, when an unknown printer took a galley of type and
						scrambled it to make a type specimen book.
					</p>
					<hr className="my-4" />
					<p>
						Lorem Ipsum has been the industry's standard dummy text ever since
						the 1500s, when an unknown printer took a galley of type and
						scrambled it to make a type specimen book. It has survived not only
						five centuries, but also the leap into electronic typesetting,
						remaining essentially unchanged.
					</p>
					<p className="lead">
						<Link to="/movies" className="btn btn-primary btn-lg">
							See all our movies
						</Link>
					</p>
				</Jumbotron>
				<MoviesFilter />
				<MoviesView />
			</div>
		);
	}
}

export default HomePage;
