import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import Jumbotron from '../components/layouts/Jumbotron';

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
				<div className="container py-3">
					<h2>Become a member today!</h2>
					<hr />
					<div className="card-deck mb-0 text-center">
						<div className="card mb-4 mx-5 box-shadow">
							<div className="card-header">
								<h4 className="my-0 font-weight-normal">Membership</h4>
							</div>
							<div className="card-body">
								<h1 className="card-title pricing-card-title">
									$10 <small className="text-muted">/ mo</small>
								</h1>
								<ul className="list-unstyled mt-3 mb-4">
									<li>Full purchase history</li>
									<li>10% discount</li>
									<li>Point system</li>
								</ul>
								<Link to="signup" className="btn btn-lg btn-block btn-primary">
									Become a member
								</Link>
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

export default HomePage;
