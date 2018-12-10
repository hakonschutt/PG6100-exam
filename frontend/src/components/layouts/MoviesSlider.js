import React, { Component } from 'react';
import { connect } from 'react-redux';

import { fetchMovies } from '../../actions';
import Slider from '../helpers/Slider';
import MovieList from './MovieList';

class MoviesSlider extends Component {
	componentDidMount() {
		this.props.fetchMovies();
	}

	render() {
		return (
			<div className="wrap clearfix">
				<Slider>
					<MovieList />
				</Slider>
			</div>
		);
	}
}

export default connect(
	null,
	{ fetchMovies }
)(MoviesSlider);
