import React from 'react';

import Logo from '../../assets/imgs/logo_white.svg';

const Footer = () => {
	return (
		<footer className="page-footer font-small teal pt-4 bg-dark">
			<div className="container py-5">
				<div className="container-fluid text-center text-md-left">
					<div className="row">
						<div className="col-md-4 mt-md-0 mt-3 text-white">
							<img src={Logo} alt="house of movies logo" />
						</div>
						<hr className="clearfix w-100 d-md-none pb-3" />
						<div className="col-md-4 mt-md-0 mt-3 text-white">
							<h5 className="text-uppercase font-weight-bold">
								Customer service
							</h5>
							<ul className="list-unstyled">
								<li>Frequently asked questions</li>
								<li>Rental</li>
								<li>Sign up for movie news</li>
							</ul>
						</div>
						<hr className="clearfix w-100 d-md-none pb-3" />
						<div className="col-md-4 mb-md-0 mb-3 text-white">
							<h5 className="text-uppercase font-weight-bold">Policies</h5>
							<ul className="list-unstyled">
								<li>Privacy policy</li>
								<li>Cookie policy</li>
								<li>Member policy</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div className="footer-copyright text-center py-3 bg-secondary">
				<div className="container">
					©{new Date().getFullYear()} Copyright: House of movies
				</div>
			</div>
		</footer>
	);
};

export default Footer;
