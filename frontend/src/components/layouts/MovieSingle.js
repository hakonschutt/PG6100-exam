import React from 'react';
import { Link } from 'react-router-dom';

const MovieSingle = ({
	id,
	coverArt,
	title,
	overview,
	releaseDate,
	isReleased = true,
}) => {
	return (
		<div className="col-lg-4 col-md-4 col-sm-4 col-xs-12">
			<div className="card">
				<img
					src={`https://image.tmdb.org/t/p/w500${coverArt}`}
					className="card-img-top"
					alt={`${title} cover art`}
				/>
				<div className="card-body">
					<h5 className="card-title">{title}</h5>
					<p className="card-text">{overview}</p>
					<p className="card-text">
						<small className="text-muted">
							{isReleased ? 'Released: ' : 'Releases: '}
							{releaseDate}
						</small>
					</p>
				</div>
				<div className="card-footer">
					<Link className="btn btn-primary d-block" to={`/movies/${id}`}>
						More info
					</Link>
				</div>
			</div>
		</div>
	);
};

export default MovieSingle;
