import React from 'react';

import Logo from '../../assets/imgs/logo_white.svg';

const Footer = () => {
	return (
		<footer>
			<div className="wrap">
				<div className="footer-top">
					<div>
						<div className="img-wrap">
							<img src={Logo} alt="house of movies logo" />
						</div>
					</div>
					<div>
						<h3>Customer service</h3>
						<ul>
							<li>Frequently asked questions</li>
							<li>Rental</li>
							<li>Sign up for movie news</li>
						</ul>
					</div>
					<div>
						<h3>Policies</h3>
						<ul>
							<li>Privacy policy</li>
							<li>Cookie policy</li>
							<li>Member policy</li>
						</ul>
					</div>
				</div>
				<div className="footer-bottom">
					<span>©{new Date().getFullYear()} House of movies</span>
				</div>
			</div>
		</footer>
	);
};

export default Footer;
