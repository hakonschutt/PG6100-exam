import React, { Component } from 'react';
// import StatementImage from '../components/layouts/StatementImage';
//
// import Deadpool from '../assets/imgs/deadpool.jpg';
import MoviesSlider from '../components/layouts/MoviesSlider';

class HomePage extends Component {
	render() {
		// return (
		// 	<StatementImage img={Deadpool} side="left">
		// 		<div className="text-info">
		// 			<h1>Deadpool 2</h1>
		// 			<div>
		// 				<span>
		// 					Lorem Ipsum has been the industry
		// 					{"'"}s standard dummy text ever since the 1500s, when an unknown
		// 					printer took a galley of type and scrambled it to make a type
		// 					specimen book. It has survived not only five centuries, but also
		// 					the leap into electronic typesetting, remaining essentially
		// 					unchanged.
		// 				</span>
		// 			</div>
		// 		</div>
		// 	</StatementImage>
		// );

		return (
			<div>
				<MoviesSlider />
			</div>
		);
	}
}

export default HomePage;
