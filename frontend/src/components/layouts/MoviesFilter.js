import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm, Field } from 'redux-form';

import SingleDayField from '../forms/SingleDayField';
import SelectField from '../forms/SelectField';
import { formatMovieListToOptions } from '../../utils';

class MoviesFilter extends Component {
	onSubmit(fields) {}

	render() {
		const movieOption = formatMovieListToOptions(this.props.movies.list);
		const venueOption = [];

		return (
			<div className="movies-filter">
				<div className="wrap">
					<form onSubmit={this.props.handleSubmit(this.onSubmit.bind(this))}>
						<Field
							label="Select movie"
							component={SelectField}
							type="text"
							name="movie"
							placeholder="Movie"
							options={movieOption || []}
						/>
						<Field
							label="Select a day"
							component={SingleDayField}
							type="text"
							name="day"
						/>
						<Field
							label="Select a venue"
							component={SelectField}
							type="text"
							name="venue"
							placeholder="Venue"
							options={venueOption || []}
						/>
						<button className="form-button" type="submit">
							Filter
						</button>
					</form>
				</div>
			</div>
		);
	}
}

function mapStateToProps({ movies }) {
	return { movies };
}

export default reduxForm({
	form: 'moviesFilter',
})(connect(mapStateToProps)(MoviesFilter));
